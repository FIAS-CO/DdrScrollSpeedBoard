package com.fias.ddrhighspeed

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fias.ddrhighspeed.util.LongLongTapper
import com.google.android.material.textfield.TextInputEditText
import junit.framework.AssertionFailedError
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@LargeTest
@RunWith(AndroidJUnit4::class)
class EstimateByNameFragmentTest {

    private val waitMills: Long = 350

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        launchFragmentInContainer<ScrollSpeedBoardFragment>()
        goToSearchFragment()
    }

    @Test
    fun goToDetail_3行表示_詳細表示後にスクロールスピード変更_backToSearch() {
        assertIsInSearchMode()

        editTextAndWait("200")
        writeSearchWord("ace for")
        clickSearchedSongInPosition(2)

        assertIsInDetailMode("ACE FOR ACES(Challenge)")

        editTextAndWait("500")

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "800.0", "0.5", "400.0")))
            .check(matches(atPositionOnResultRow(1, "基本①", "400.0", "1.25", "500.0")))
            .check(matches(atPositionOnResultRow(2, "最小", "50.0", "8.0", "400.0")))

        clickBackButton()
        assertIsInSearchMode()
    }

    @Test
    fun goToDetail_1行表示_詳細表示前にスクロールスピード変更() {
        assertIsInSearchMode()

        editTextAndWait("600")
        writeSearchWord("ace for")
        clickSearchedSongInPosition(0)

        assertIsInDetailMode("ACE FOR ACES(Basic)")

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "基本①", "200.0", "3.0", "600.0")))
    }

    @Test
    fun goToDetail_4行表示_スクロールスピード2000から2001へ_UpSpinButton使用() {
        assertIsInSearchMode()
        editTextAndWait("2000")

        writeSearchWord("888")
        clickSearchedSongInPosition(0)

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "4.5", "1998.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "4.5", "1998.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "8.0", "1776.0")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "8.0", "888.0")))

        getUpSpinButton().perform(click())

        onView(withId(R.id.text_input_edit_text)).check(matches(withText("2001")))
        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "-", "-")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "-", "-")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "-", "-")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "-", "-")))
    }

    @Test
    fun goToDetail_1行表示_スクロールスピード30から29へ_DownSpinButton使用() {
        assertIsInSearchMode()
        editTextAndWait("30")

        writeSearchWord("888")
        clickSearchedSongInPosition(0)

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "0.25", "111.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "0.25", "111.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "0.25", "55.5")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "0.25", "27.75")))

        getDownSpinButton().perform(click())

        onView(withId(R.id.text_input_edit_text)).check(matches(withText("29")))

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "-", "-")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "-", "-")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "-", "-")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "-", "-")))
    }

    @Test
    fun upSpinButton長押し_scrollSpeedがプラス30程度されrecyclerViewが更新される() {
        assertIsInSearchMode()
        editTextAndWait("600").checkText("600")

        writeSearchWord("888")
        clickSearchedSongInPosition(0)

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "2.5", "555.0")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "5.0", "555.0")))

        longClickUpSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text)).check { view, noViewFoundException ->
            if (noViewFoundException != null) throw noViewFoundException

            val input = (view as TextInputEditText).text.toString().toDouble()
            assertThat("", input, Matchers.closeTo(631.0, 633.0))
        }

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "2.75", "610.5")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "5.5", "610.5")))
    }

    @Test
    fun upSpinButton長押し_scrollSpeedがマイナス30程度されrecyclerViewが更新される() {
        assertIsInSearchMode()
        editTextAndWait("600").checkText("600")

        writeSearchWord("888")
        clickSearchedSongInPosition(0)

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "2.5", "555.0")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "5.0", "555.0")))

        longClickDownSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text)).check { view, noViewFoundException ->
            if (noViewFoundException != null) throw noViewFoundException

            val input = (view as TextInputEditText).text.toString().toDouble()
            assertThat("", input, Matchers.closeTo(567.0, 569.0))
        }

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "2.5", "555.0")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "5.0", "555.0")))
    }

    @Test
    fun downSpinButton長押し_scrollSpeedがマイナス30程度されrecyclerViewが更新される() {
        assertIsInSearchMode()
        editTextAndWait("600").checkText("600")

        writeSearchWord("888")
        clickSearchedSongInPosition(0)

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "2.5", "555.0")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "5.0", "555.0")))

        longClickDownSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text)).check { view, noViewFoundException ->
            if (noViewFoundException != null) throw noViewFoundException

            val input = (view as TextInputEditText).text.toString().toDouble()
            assertThat("", input, Matchers.closeTo(567.0, 569.0))
        }

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(1, "基本②", "444.0", "1.25", "555.0")))
            .check(matches(atPositionOnResultRow(2, "基本①", "222.0", "2.5", "555.0")))
            .check(matches(atPositionOnResultRow(3, "最小", "111.0", "5.0", "555.0")))
    }

    @Test
    fun goToDetail_データベースバージョン2で追加された曲が表示される() {
        assertIsInSearchMode()

        editTextAndWait("700")
        writeSearchWord("STAY G")
        clickSearchedSongInPosition(0)

        assertIsInDetailMode("STAY GOLD")

        getDetailSongList()
            .check(matches(atPositionOnResultRow(0, "最大", "330.0", "2.0", "660.0")))
            .check(matches(atPositionOnResultRow(1, "基本①", "165.0", "4.0", "660.0")))
            .check(matches(atPositionOnResultRow(2, "最小", "41.0", "8.0", "328.0")))
    }

    @Test
    fun 広告が表示されている() {
        var failureCount = 0
        Thread.sleep(300)

        while (true) {
            try {
                onView(withId(R.id.adView)).check(matches(isDisplayed()))
                return
            } catch (_: AssertionFailedError) {
                failureCount += 1
                Thread.sleep(300)

                if (failureCount >= 10) {
                    Assert.fail("3秒待ちましたが、広告が isDisplayed になりませんでした。")
                }
            }
        }
    }

    //region private methods

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    private fun editTextAndWait(value: String): ViewInteraction {
        val perform = onView(withId(R.id.text_input_edit_text)).perform(replaceText(value))

        Thread.sleep(waitMills)

        return perform
    }

    private fun assertIsInSearchMode() {
        assertSearchWordIsDisplayed()
        assertSearchedSongListIsDisplayed()

        assertHeaderIs_Not_Displayed()
        assertSongNameInDetailIs_Not_Displayed()
        assertBackButtonIs_Not_Displayed()
        assertDetailBpmListIs_Not_Displayed()
        assertAsteriskIs_Not_Displayed()
        assertExplainBasicTextIs_Not_Displayed()
    }

    private fun assertIsInDetailMode(songName: String) {
        assertSearchWordIs_Not_Displayed()
        assertSearchedSongListIs_Not_Displayed()

        assertHeaderIsDisplayed()
        assertSongNameInDetail(songName)
        assertBackButtonIsDisplayed()
        assertDetailBpmListIsDisplayed()
        assertAsteriskIsDisplayed()
        assertExplainBasicTextIsDisplayed()
    }

    private fun goToSearchFragment() {
        val tabView = onView(
            allOf(
                withContentDescription("曲名検索"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tab_layout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())
    }

    private fun writeSearchWord(word: String) {
        val searchWordView = getSearchWordView()
        searchWordView.perform(replaceText(word), closeSoftKeyboard())
    }

    private fun assertSearchWordIsDisplayed() {
        val searchWordView = getSearchWordView()
        searchWordView.check(matches(isDisplayed()))
    }

    private fun assertSearchWordIs_Not_Displayed() {
        val searchWordView = getSearchWordView()
        searchWordView.check(matches(not(isDisplayed())))
    }

    private fun getSearchWordView() = onView(
        allOf(
            withId(R.id.search_word_input),
            childAtPosition(
                childAtPosition(
                    withId(R.id.search_text_field),
                    0
                ),
                0
            )
        )
    )

    private fun getUpSpinButton(): ViewInteraction = onView(
        allOf(
            withId(R.id.increment_up), withText("▲"),
            childAtPosition(
                allOf(
                    withId(R.id.input_layout),
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    )
                ),
                1
            ),
            isDisplayed()
        )
    )

    private fun getDownSpinButton(): ViewInteraction = onView(
        allOf(
            withId(R.id.increment_down), withText("▼"),
            childAtPosition(
                allOf(
                    withId(R.id.input_layout),
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    )
                ),
                2
            ),
            isDisplayed()
        )
    )

    private fun assertSearchedSongListIsDisplayed() {
        getSearchedSongListView().check(matches(isDisplayed()))
    }

    private fun assertSearchedSongListIs_Not_Displayed() {
        getSearchedSongListView().check(matches(not(isDisplayed())))
    }

    private fun getSearchedSongListView() = onView(withId(R.id.searched_songs))

    private fun clickSearchedSongInPosition(position: Int) {
        getSearchedSongs().perform(actionOnItemAtPosition<ViewHolder>(position, click()))
    }

    private fun getSearchedSongs() = onView(
        allOf(
            withId(R.id.searched_songs),
            childAtPosition(
                withId(R.id.search_layout),
                2
            )
        )
    )

    private fun assertSongNameInDetail(songName: String) {
        getDetailSongName().check(matches(withText(songName)))
    }

    private fun assertSongNameInDetailIs_Not_Displayed() {
        getDetailSongName().check(matches(withEffectiveVisibility(Visibility.GONE)))

    }

    private fun getDetailSongName() = onView(
        allOf(
            withId(R.id.song_name),
            withParent(
                allOf(
                    withId(R.id.search_layout),
                    withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))
                )
            )
        )
    )

    private fun assertHeaderIsDisplayed() {
        getBlankHeader().check(matches(withText("")))
        getBpmHeader().check(matches(withText("BPM")))
        getHighSpeedHeader().check(matches(withText("ﾊｲｽﾋﾟ")))
        getScrollSpeedHeader().check(matches(withText("ｽｸﾛｰﾙｽﾋﾟｰﾄﾞ")))
    }

    private fun assertHeaderIs_Not_Displayed() {
        getBlankHeader().check(matches(withEffectiveVisibility(Visibility.GONE)))
        getBpmHeader().check(matches(withEffectiveVisibility(Visibility.GONE)))
        getHighSpeedHeader().check(matches(withEffectiveVisibility(Visibility.GONE)))
        getScrollSpeedHeader().check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    private fun getScrollSpeedHeader() = onView(
        allOf(
            withId(R.id.scroll_speed_header), withText("ｽｸﾛｰﾙｽﾋﾟｰﾄﾞ"),
            withParent(
                allOf(
                    withId(R.id.song_detail_table_header),
                    withParent(withId(R.id.search_layout))
                )
            )
        )
    )

    private fun getHighSpeedHeader() = onView(
        allOf(
            withId(R.id.high_speed_header), withText("ﾊｲｽﾋﾟ"),
            withParent(
                allOf(
                    withId(R.id.song_detail_table_header),
                    withParent(withId(R.id.search_layout))
                )
            )
        )
    )

    private fun getBpmHeader() = onView(
        allOf(
            withId(R.id.bpm_header),// withText("BPM"),
            withParent(
                allOf(
                    withId(R.id.song_detail_table_header),
                    withParent(withId(R.id.search_layout))
                )
            )
        )
    )

    private fun getBlankHeader() = onView(
        allOf(
            withId(R.id.blank),
            withParent(
                allOf(
                    withId(R.id.song_detail_table_header),
                    withParent(withId(R.id.search_layout))
                )
            )
        )
    )

    private fun clickBackButton() {
        getBackButton().perform(click())
    }

    private fun assertBackButtonIsDisplayed() {
        getBackButton().check(matches(isDisplayed()))
    }

    private fun assertBackButtonIs_Not_Displayed() {
        getBackButton().check(matches(not(isDisplayed())))
    }

    private fun getBackButton(): ViewInteraction = onView(
        allOf(
            withId(R.id.back_to_search_image), withContentDescription("back"),
            withParent(
                allOf(
                    withId(R.id.search_layout),
                    withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))
                )
            )
        )
    )

    private fun assertDetailBpmListIsDisplayed() {
        getDetailSongList().check(matches(isDisplayed()))
    }

    private fun assertDetailBpmListIs_Not_Displayed() {
        getDetailSongList().check(matches(not(isDisplayed())))
    }

    private fun getDetailSongList() = onView(
        allOf(
            withId(R.id.song_detail_list),
            withParent(
                allOf(
                    withId(R.id.search_layout),
                    withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))
                )
            )
        )
    )

    private fun assertAsteriskIsDisplayed() {
        getAsterisk().check(matches(isDisplayed()))
    }

    private fun assertAsteriskIs_Not_Displayed() {
        getAsterisk().check(matches(not(isDisplayed())))
    }

    private fun getAsterisk() = onView(withId(R.id.asterisk))

    private fun assertExplainBasicTextIsDisplayed() {
        getExplainBasicText().check(matches(isDisplayed()))
    }

    private fun assertExplainBasicTextIs_Not_Displayed() {
        getExplainBasicText().check(matches(not(isDisplayed())))
    }

    private fun getExplainBasicText() = onView(withId(R.id.explain_basic_text))

    private fun atPositionOnResultRow(
        position: Int,
        expectedCategory: String,
        expectedBpm: String,
        expectedHighSpeed: String,
        expectedScrollSpeed: String
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            lateinit var categoryView: TextView
            lateinit var bpmView: TextView
            lateinit var highSpeedView: TextView
            lateinit var scrollSpeedView: TextView

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)

                categoryView = viewHolder!!.itemView.findViewById<View>(R.id.category) as TextView
                val categoryCorrect = withText(expectedCategory).matches(categoryView)

                bpmView = viewHolder.itemView.findViewById<View>(R.id.bpm_view) as TextView
                val bpmCorrect = withText(expectedBpm).matches(bpmView)

                highSpeedView =
                    viewHolder.itemView.findViewById<View>(R.id.high_speed_view) as TextView
                val highSpeedCorrect = withText(expectedHighSpeed).matches(highSpeedView)

                scrollSpeedView =
                    viewHolder.itemView.findViewById<View>(R.id.scroll_speed_view) as TextView
                val scrollSpeedCorrect = withText(expectedScrollSpeed).matches(scrollSpeedView)

                return categoryCorrect && bpmCorrect && highSpeedCorrect && scrollSpeedCorrect
            }

            override fun describeTo(description: Description) {
                description.appendText(
                    "category = $expectedCategory, bpm = $expectedBpm, highSpeed = $expectedHighSpeed, scrollSpeed = $expectedScrollSpeed"
                )
            }

            override fun describeMismatch(item: Any?, description: Description) {
                super.describeMismatch(item, description)
                description.appendText(
                    "category = ${categoryView.text}, bpm = ${bpmView.text}, highSpeed = ${highSpeedView.text}, scrollSpeed = ${scrollSpeedView.text} at position $position"
                )
            }
        }
    }

    private fun ViewInteraction.checkText(value: String) = apply {
        this.check(matches(withText(value)))
    }

    private fun longClickUpSpinButtonAndWait() {
        onView(withId(R.id.increment_up)).perform(longLongClick())

        Thread.sleep(waitMills)
    }

    private fun longClickDownSpinButtonAndWait() {
        onView(withId(R.id.increment_down)).perform(longLongClick())

        Thread.sleep(waitMills)
    }

    // 既存の longClick() の長さが足りないので使用
    private fun longLongClick(): ViewAction {
        return actionWithAssertions(
            GeneralClickAction(
                LongLongTapper(),
                GeneralLocation.CENTER,
                Press.FINGER,
                InputDevice.SOURCE_UNKNOWN,
                MotionEvent.BUTTON_PRIMARY
            )
        )
    }
    //endregion
}

package com.fias.ddrhighspeed

import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.textfield.TextInputEditText
import com.google.common.truth.Truth.assertThat
import junit.framework.AssertionFailedError
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.closeTo
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
class ScrollSpeedBoardFragmentTest : FragmentTestBase() {

    private val errorMessage = "30 ～ 2000までの数値を入力してください。"

    @Before
    fun setup() {
        launchFragmentInContainer<ScrollSpeedBoardFragment>()
    }

    /**
     * 基本方針
     * RecyclerView は一番上と一番下の要素をチェックする
     */
    @Test
    fun scrollSpeed入力_recyclerViewが表示される() {
        editTextAndWait("500").checkText("500")

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1 ～ 62", "8.0", "8.0 ～ 496.0")))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(matches(atPositionOnResultRow(23, "1001 ～ 2000", "0.25", "250.25 ～ 500.0")))
    }

    /**
     * 専用表示：bpm と ScrollSpeed が "-" になる(暫定)
     */
    @Test
    fun scrollSpeed入力_2001以上でリストが専用表示() {
        editTextAndWait("2001").checkTextWithError("2001", errorMessage)

        checkRecyclerViewOnError()
    }

    @Test
    fun scrollSpeed入力_29以上でリストが専用表示() {
        editTextAndWait("29").checkTextWithError("29", errorMessage)

        checkRecyclerViewOnError()
    }

    @Test
    fun scrollSpeed入力_空文字でリストが専用表示() {
        editTextAndWait("").checkTextWithError("", errorMessage)

        checkRecyclerViewOnError()
    }

    /**
     * テストだと入力ができてしまう。
     * 実際は本来は入力できない設定
     */
    @Test
    fun scrollSpeed入力_数字以外が入力できない() {
        editTextAndWait("abc666").checkTextWithError("abc666", errorMessage)

        checkRecyclerViewOnError()
    }

    @Test
    fun scrollSpeed入力_エラー状態で範囲内の値を入力するとエラーが消える() {
        editTextAndWait("2001").checkTextWithError("2001", errorMessage)

        editTextAndWait("500").checkText("500")

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPositionOnResultRow(0, "1 ～ 62", "8.0", "8.0 ～ 496.0")))
//            .perform(ScrollToBottomAction())
//            .check(matches(atPositionOnResultRow(23, "1001 ～ 2000", "0.25", "250.25 ～ 500.0")))

    }

    @Test
    fun upSpinButton押下_scrollSpeedがインクリメントされrecyclerViewが更新される() {
        editTextAndWait("600")

        clickUpSpinButtonAndWait()

        getScrollSpeedTextEdit().checkText("601")
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1 ～ 75", "8.0", "8.0 ～ 600.0")))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(matches(atPositionOnResultRow(23, "1203 ～ 2404", "0.25", "300.75 ～ 601.0")))

    }

    @Test
    fun upSpinButton押下_scrollSpeedが範囲外になりエラーが表示される() {
        editTextAndWait("2000")

        clickUpSpinButtonAndWait()

        getScrollSpeedTextEdit().checkTextWithError("2001", errorMessage)
        checkRecyclerViewOnError()
    }

    @Test
    fun upSpinButton押下_scrollSpeedが範囲内になりエラーが消える() {
        editTextAndWait("29").checkTextWithError("29", errorMessage)

        clickUpSpinButtonAndWait()

        getScrollSpeedTextEdit().checkText("30")
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1 ～ 3", "8.0", "8.0 ～ 24.0")))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(matches(atPositionOnResultRow(23, "61 ～ 120", "0.25", "15.25 ～ 30.0")))
    }

    @Test
    fun downSpinButton押下_scrollSpeedがインクリメントされrecyclerViewが更新される() {
        editTextAndWait("600").checkText("600")

        clickDownSpinButtonAndWait()

        getScrollSpeedTextEdit().checkText("599")
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1 ～ 74", "8.0", "8.0 ～ 592.0")))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(matches(atPositionOnResultRow(23, "1199 ～ 2396", "0.25", "299.75 ～ 599.0")))
    }

    @Test
    fun downSpinButton押下_scrollSpeedが範囲外になりエラーが表示される() {
        editTextAndWait("30").checkText("30")

        clickDownSpinButtonAndWait()

        getScrollSpeedTextEdit().checkTextWithError("29", errorMessage)
        checkRecyclerViewOnError()
    }

    @Test
    fun downSpinButton押下_scrollSpeedが範囲内になりエラーが消える() {
        editTextAndWait("2001").checkTextWithError("2001", errorMessage)

        clickDownSpinButtonAndWait()

        getScrollSpeedTextEdit().checkText("2000")
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1 ～ 250", "8.0", "8.0 ～ 2000.0")))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(matches(atPositionOnResultRow(23, "4001 ～ 8000", "0.25", "1000.25 ～ 2000.0")))
    }

    @Test
    fun upSpinButton長押し_scrollSpeedがプラス30程度されrecyclerViewが更新される() {
        editTextAndWait("600").checkText("600")

        longClickUpSpinButtonAndWait()

        var input: Double = -1.0

        getScrollSpeedTextEdit().check { view, noViewFoundException ->
            if (noViewFoundException != null) throw noViewFoundException

            input = (view as TextInputEditText).text.toString().toDouble()
            assertThat("", input, closeTo(631.0, 633.0))
        }.check(matches(withNoError()))
        checkRecyclerView(input)
    }

    @Test
    fun downSpinButton長押し_scrollSpeedがマイナス30程度されrecyclerViewが更新される() {
        editTextAndWait("600").checkText("600")

        longClickDownSpinButtonAndWait()

        var input: Double = -1.0

        getScrollSpeedTextEdit().check { view, noViewFoundException ->
            if (noViewFoundException != null) throw noViewFoundException

            input = (view as TextInputEditText).text.toString().toDouble()
            assertThat("", input, closeTo(567.0, 569.0))
        }.check(matches(withNoError()))

        checkRecyclerView(input)
    }

    @Test
    fun テキストエリアクリックでキーボード表示_エンター押下でキーボード非表示() {
        editTextAndWait("600").check { view, _ ->
            assertThat(view.hasFocus()).isEqualTo(false)
        }.perform(click()).check { view, _ ->
            assertThat(view.hasFocus()).isEqualTo(true)
        }.perform(pressImeActionButton()).check { view, _ ->
            assertThat(view.hasFocus()).isEqualTo(false)
        }
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
                    fail("3秒待ちましたが、広告が isDisplayed になりませんでした。")
                }
            }
        }
    }

    private fun checkRecyclerView(input: Double) {
        val maxHighSpeed = (input / 8).toInt()
        val minHighSpeed = 1
        val maxScrollSpeed = maxHighSpeed.toDouble() * 8
        val minScrollSpeed = minHighSpeed.toDouble() * 8

//        val maxHighSpeed2 = (input / 0.25).toInt()
//        val minHighSpeed2 = (input / 0.5).toInt() + 1
//        val maxScrollSpeed2 = maxHighSpeed2.toDouble() * 0.25
//        val minScrollSpeed2 = minHighSpeed2.toDouble() * 0.25

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0)).check(
                matches(
                    atPositionOnResultRow(
                        0,
                        "$minHighSpeed ～ $maxHighSpeed",
                        "8.0",
                        "$minScrollSpeed ～ $maxScrollSpeed"
                    )
                )
            )
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(
//                matches(
//                    atPositionOnResultRow(
//                        23,
//                        "$minHighSpeed2 ～ $maxHighSpeed2",
//                        "0.25",
//                        "$minScrollSpeed2 ～ $maxScrollSpeed2"
//                    )
//                )
//            )
    }

    private fun checkRecyclerViewOnError() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "-", "8.0", "-")))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
//            .check(matches(atPositionOnResultRow(23, "-", "0.25", "-")))
    }

    private fun atPositionOnResultRow(
        position: Int, expectedBpm: String, expectedHighSpeed: String, expectedScrollSpeed: String
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            lateinit var bpmView: TextView
            lateinit var highSpeedView: TextView
            lateinit var scrollSpeedView: TextView

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)

                bpmView = viewHolder!!.itemView.findViewById<View>(R.id.bpm_view) as TextView
                val bpmCorrect = withText(expectedBpm).matches(bpmView)

                highSpeedView =
                    viewHolder.itemView.findViewById<View>(R.id.high_speed_view) as TextView
                val highSpeedCorrect = withText(expectedHighSpeed).matches(highSpeedView)

                scrollSpeedView =
                    viewHolder.itemView.findViewById<View>(R.id.scroll_speed_view) as TextView
                val scrollSpeedCorrect = withText(expectedScrollSpeed).matches(scrollSpeedView)

                return bpmCorrect && highSpeedCorrect && scrollSpeedCorrect
            }

            override fun describeTo(description: Description) {
                description.appendText(
                    "bpm = $expectedBpm, highSpeed = $expectedHighSpeed, scrollSpeed = $expectedScrollSpeed"
                )
            }

            override fun describeMismatch(item: Any?, description: Description) {
                super.describeMismatch(item, description)
                description.appendText(
                    "bpm = ${bpmView.text}, highSpeed = ${highSpeedView.text}, scrollSpeed = ${scrollSpeedView.text} at position $position"
                )
            }
        }
    }

    private fun clickUpSpinButtonAndWait() {
        getUpSpinButton().perform(click())

        Thread.sleep(waitMills)
    }

    private fun clickDownSpinButtonAndWait() {
        getDownSpinButton().perform(click())

        Thread.sleep(waitMills)
    }
}

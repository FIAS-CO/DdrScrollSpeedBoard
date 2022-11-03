package com.fias.ddrhighspeed

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fias.ddrhighspeed.util.LongLongTapper
import com.google.android.material.textfield.TextInputEditText
import com.google.common.truth.Truth.assertThat
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.closeTo
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
class ScrollSpeedBoardFragmentTest {

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
        editTextAndWait("500")
            .checkText("500")

        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1001 ～ 2000", "0.25", "250.25 ～ 500.0")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "1 ～ 62", "8.0", "8.0 ～ 496.0")))
    }

    /**
     * 専用表示：bpm と ScrollSpeed が "-" になる(暫定)
     */
    @Test
    fun scrollSpeed入力_2001以上でリストが専用表示() {
        editTextAndWait("2001")
            .checkTextWithError("2001", errorMessage)

        checkRecyclerViewOnError()
    }

    @Test
    fun scrollSpeed入力_29以上でリストが専用表示() {
        editTextAndWait("29")
            .checkTextWithError("29", errorMessage)

        checkRecyclerViewOnError()
    }

    @Test
    fun scrollSpeed入力_空文字でリストが専用表示() {
        editTextAndWait("")
            .checkTextWithError("", errorMessage)

        checkRecyclerViewOnError()
    }

    /**
     * テストだと入力ができてしまう。
     * 実際は本来は入力できない設定
     */
    @Test
    fun scrollSpeed入力_数字以外が入力できない() {
        editTextAndWait("abc666")
            .checkTextWithError("abc666", errorMessage)

        checkRecyclerViewOnError()
    }

    @Test
    fun scrollSpeed入力_エラー状態で範囲内の値を入力するとエラーが消える() {
        editTextAndWait("2001")
            .checkTextWithError("2001", errorMessage)

        editTextAndWait("500")
            .checkText("500")

        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1001 ～ 2000", "0.25", "250.25 ～ 500.0")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "1 ～ 62", "8.0", "8.0 ～ 496.0")))
    }

    @Test
    fun upSpinButton押下_scrollSpeedがインクリメントされrecyclerViewが更新される() {
        editTextAndWait("600")

        clickUpSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text))
            .checkText("601")
        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1203 ～ 2404", "0.25", "300.75 ～ 601.0")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "1 ～ 75", "8.0", "8.0 ～ 600.0")))
    }

    @Test
    fun upSpinButton押下_scrollSpeedが範囲外になりエラーが表示される() {
        editTextAndWait("2000")

        clickUpSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text))
            .checkTextWithError("2001", errorMessage)
        checkRecyclerViewOnError()
    }

    @Test
    fun upSpinButton押下_scrollSpeedが範囲内になりエラーが消える() {
        editTextAndWait("29")
            .checkTextWithError("29", errorMessage)

        clickUpSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text))
            .checkText("30")
        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "61 ～ 120", "0.25", "15.25 ～ 30.0")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "1 ～ 3", "8.0", "8.0 ～ 24.0")))
    }

    @Test
    fun downSpinButton押下_scrollSpeedがインクリメントされrecyclerViewが更新される() {
        editTextAndWait("600")
            .checkText("600")

        clickDownSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text))
            .checkText("599")
        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "1199 ～ 2396", "0.25", "299.75 ～ 599.0")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "1 ～ 74", "8.0", "8.0 ～ 592.0")))
    }

    @Test
    fun downSpinButton押下_scrollSpeedが範囲外になりエラーが表示される() {
        editTextAndWait("30")
            .checkText("30")

        clickDownSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text))
            .checkTextWithError("29", errorMessage)
        checkRecyclerViewOnError()
    }

    @Test
    fun downSpinButton押下_scrollSpeedが範囲内になりエラーが消える() {
        editTextAndWait("2001")
            .checkTextWithError("2001", errorMessage)

        clickDownSpinButtonAndWait()

        onView(withId(R.id.text_input_edit_text))
            .checkText("2000")
        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "4001 ～ 8000", "0.25", "1000.25 ～ 2000.0")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "1 ～ 250", "8.0", "8.0 ～ 2000.0")))
    }

    @Test
    fun upSpinButton長押し_scrollSpeedがプラス30程度されrecyclerViewが更新される() {
        editTextAndWait("600")
            .checkText("600")

        longClickUpSpinButtonAndWait()

        var input: Double = -1.0

        onView(withId(R.id.text_input_edit_text))
            .check { view, noViewFoundException ->
                if (noViewFoundException != null) throw noViewFoundException

                input = (view as TextInputEditText).text.toString().toDouble()
                assertThat("", input, closeTo(631.0, 633.0))
            }
            .check(matches(withNoError()))
        checkRecyclerView(input)
    }

    @Test
    fun downSpinButton長押し_scrollSpeedがマイナス30程度されrecyclerViewが更新される() {
        editTextAndWait("600")
            .checkText("600")

        longClickDownSpinButtonAndWait()

        var input: Double = -1.0

        onView(withId(R.id.text_input_edit_text))
            .check { view, noViewFoundException ->
                if (noViewFoundException != null) throw noViewFoundException

                input = (view as TextInputEditText).text.toString().toDouble()
                assertThat("", input, closeTo(567.0, 569.0))
            }
            .check(matches(withNoError()))

        checkRecyclerView(input)
    }

    @Test
    fun テキストエリアクリックでキーボード表示_エンター押下でキーボード非表示() {
        editTextAndWait("600")
            .check { view, _ ->
                assertThat(view.hasFocus()).isEqualTo(false)
            }
            .perform(click())
            .check { view, _ ->
                assertThat(view.hasFocus()).isEqualTo(true)
            }
            .perform(pressImeActionButton())
            .check { view, _ ->
                assertThat(view.hasFocus()).isEqualTo(false)
            }
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

    private fun checkRecyclerView(input: Double) {
        val maxHighSpeed = (input / 0.25).toInt()
        val minHighSpeed = (input / 0.5).toInt() + 1
        val maxScrollSpeed = maxHighSpeed.toDouble() * 0.25
        val minScrollSpeed = minHighSpeed.toDouble() * 0.25

        val maxHighSpeed2 = (input / 8).toInt()
        val minHighSpeed2 = 1
        val maxScrollSpeed2 = maxHighSpeed2.toDouble() * 8
        val minScrollSpeed2 = minHighSpeed2.toDouble() * 8

        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(
                matches(
                    atPositionOnResultRow(
                        0,
                        "$minHighSpeed ～ $maxHighSpeed",
                        "0.25",
                        "$minScrollSpeed ～ $maxScrollSpeed"
                    )
                )
            )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(
                matches(
                    atPositionOnResultRow(
                        23,
                        "$minHighSpeed2 ～ $maxHighSpeed2",
                        "8.0",
                        "$minScrollSpeed2 ～ $maxScrollSpeed2"
                    )
                )
            )
    }

    private fun checkRecyclerViewOnError() {
        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPositionOnResultRow(0, "-", "0.25", "-")))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(23))
            .check(matches(atPositionOnResultRow(23, "-", "8.0", "-")))
    }

    private fun atPositionOnResultRow(
        position: Int,
        expectedBpm: String,
        expectedHighSpeed: String,
        expectedScrollSpeed: String
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
                    "bpm = $expectedBpm, highSpeed = $expectedHighSpeed," +
                            " scrollSpeed = $expectedScrollSpeed"
                )
            }

            override fun describeMismatch(item: Any?, description: Description) {
                super.describeMismatch(item, description)
                description.appendText(
                    "bpm = ${bpmView.text}, highSpeed = ${highSpeedView.text}," +
                            " scrollSpeed = ${scrollSpeedView.text} at position $position"
                )
            }
        }
    }

    private fun withNoError(): Matcher<View?> {
        return withError(null)
    }

    private fun withError(expected: String?): Matcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            override fun matchesSafely(view: View?): Boolean {
                if (view !is EditText) {
                    return false
                }
                return view.error?.toString() == expected
            }

            override fun describeTo(description: Description?) {}
        }
    }

    private fun editTextAndWait(value: String): ViewInteraction {
        val perform = onView(withId(R.id.text_input_edit_text))
            .perform(replaceText(value))

        Thread.sleep(300)

        return perform
    }

    private fun clickUpSpinButtonAndWait() {
        onView(withId(R.id.increment_up)).perform(click())

        Thread.sleep(300)
    }

    private fun clickDownSpinButtonAndWait() {
        onView(withId(R.id.increment_down)).perform(click())

        Thread.sleep(300)
    }

    private fun longClickUpSpinButtonAndWait() {
        onView(withId(R.id.increment_up)).perform(longLongClick())

        Thread.sleep(300)
    }

    private fun longClickDownSpinButtonAndWait() {
        onView(withId(R.id.increment_down)).perform(longLongClick())

        Thread.sleep(300)
    }

    private fun ViewInteraction.checkText(value: String) = apply {
        this.check(matches(withText(value)))
            .check(matches(withNoError()))
    }

    private fun ViewInteraction.checkTextWithError(value: String, errorMessage: String) {
        this
            .check(matches(withText(value)))
            .check(matches(withError(errorMessage)))
    }
}
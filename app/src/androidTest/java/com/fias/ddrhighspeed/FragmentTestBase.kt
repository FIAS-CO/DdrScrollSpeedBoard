package com.fias.ddrhighspeed

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.fias.ddrhighspeed.util.LongLongTapper
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

// TODO もっとこのクラスにメソッドを移す。主にget系
// TODO メソッド順の整理をする
open class FragmentTestBase {

    val waitMills: Long = 350

    fun editTextAndWait(value: String): ViewInteraction {
        val perform = onView(ViewMatchers.withId(R.id.text_input_edit_text))
            .perform(ViewActions.replaceText(value))

        Thread.sleep(waitMills)

        return perform
    }

    fun getScrollSpeedTextEdit(): ViewInteraction = onView(
        Matchers.allOf(
            ViewMatchers.withId(R.id.text_input_edit_text),
            ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.textField))),
            ViewMatchers.isDisplayed()
        )
    )

    fun getUpSpinButton(): ViewInteraction = onView(
        Matchers.allOf(
            ViewMatchers.withId(R.id.increment_up), ViewMatchers.withText("▲"),
            ViewMatchers.isDisplayed()
        )
    )

    fun getDownSpinButton(): ViewInteraction = onView(
        Matchers.allOf(
            ViewMatchers.withId(R.id.increment_down), ViewMatchers.withText("▼"),
            ViewMatchers.isDisplayed()
        )
    )

    fun childAtPosition(
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

    fun longClickUpSpinButtonAndWait() {
        onView(ViewMatchers.withId(R.id.increment_up)).perform(longLongClick())

        Thread.sleep(waitMills)
    }

    fun longClickDownSpinButtonAndWait() {
        onView(ViewMatchers.withId(R.id.increment_down)).perform(longLongClick())

        Thread.sleep(waitMills)
    }

    // 既存の longClick() の長さが足りないので使用
    private fun longLongClick(): ViewAction {
        return ViewActions.actionWithAssertions(
            GeneralClickAction(
                LongLongTapper(),
                GeneralLocation.CENTER,
                Press.FINGER,
                InputDevice.SOURCE_UNKNOWN,
                MotionEvent.BUTTON_PRIMARY
            )
        )
    }

    fun ViewInteraction.checkText(value: String) = apply {
        this.check(ViewAssertions.matches(ViewMatchers.withText(value))).check(
            ViewAssertions.matches(
                withNoError()
            )
        )
    }

    fun ViewInteraction.checkTextWithError(value: String, errorMessage: String) {
        this.check(ViewAssertions.matches(ViewMatchers.withText(value))).check(
            ViewAssertions.matches(
                withError(errorMessage)
            )
        )
    }

    fun withNoError(): Matcher<View?> {
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

    fun checkRecyclerViewHasSomeItem(size: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            lateinit var recyclerView: RecyclerView

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                this.recyclerView = recyclerView
                return recyclerView.adapter!!.itemCount >= size
            }

            override fun describeTo(description: Description) {
                description.appendText("Expected $size items")
            }

            override fun describeMismatch(item: Any?, description: Description) {
                super.describeMismatch(item, description)
                val actualSize = recyclerView.adapter!!.itemCount
                description.appendText("Actually $actualSize items")
            }
        }
    }
}

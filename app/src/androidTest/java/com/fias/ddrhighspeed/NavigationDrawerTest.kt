@file:Suppress("DEPRECATION")

package com.fias.ddrhighspeed

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("RemoveRedundantBackticks")
@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationDrawerTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
    }

    @Test
    fun `プライバシーポリシーをクリック`() {
        val url = Uri.parse("https://fia1988.github.io/PrivacyPolicy")

        openNavigationDrawer()

        val privacyPolicyLink = onView(
            allOf(
                withId(R.id.security_policy),
                childAtPosition(
                    allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        privacyPolicyLink.perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(url)
            )
        )
    }

    /**
     * メールアプリ選択ダイアログが開くとテストに不具合が出るので
     * 常に特定のアプリを選択するように設定し、ダイアログが出ないようにしてください
     */
    @Test
    fun `問い合わせメールをクリック`() {
        val uri = Uri.parse("mailto:")
        val address = "apps.fias@gmail.com"
        val subject = "お問い合わせ"
        val text = "ーーーーーーーーーーーーー\n" +
                "ご要望・不具合・その他なんでもご連絡ください。\n" +
                "ーーーーーーーーーーーーー\n"

        openNavigationDrawer()

        val navigationMenuItemView = onView(
            Matchers.allOf(
                withId(R.id.mail),
                childAtPosition(
                    Matchers.allOf(
                        withId(com.google.android.material.R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        intended(
            allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasData(uri),
                hasExtra(Intent.EXTRA_EMAIL, arrayOf(address)),
                hasExtra(Intent.EXTRA_SUBJECT, subject),
                hasExtra(Intent.EXTRA_TEXT, text),
            )
        )
    }

    private fun openNavigationDrawer() {
        val toolBarLeftButton = onView(
            allOf(
                // TODO 端末の言語設定によって"Open navigation drawer"である必要があるっぽい
                withContentDescription("ナビゲーション ドロワーを開く"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        toolBarLeftButton.perform(click())
    }

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
}

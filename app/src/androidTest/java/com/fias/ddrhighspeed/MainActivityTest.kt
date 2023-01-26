package com.fias.ddrhighspeed


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import junit.framework.AssertionFailedError
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

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
}

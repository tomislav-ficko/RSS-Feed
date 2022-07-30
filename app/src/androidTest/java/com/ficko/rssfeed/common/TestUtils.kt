package com.ficko.rssfeed.common

import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

object TestUtils {

    fun assertToastMessageIsDisplayed(message: String, testActivity: AppCompatActivity) {
        Espresso.onView(ViewMatchers.withText(message))
            .inRoot(RootMatchers.withDecorView(CoreMatchers.not(testActivity.window.decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
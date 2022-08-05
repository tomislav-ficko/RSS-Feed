package com.ficko.rssfeed.common

import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.test.espresso.intent.Checks
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

interface BaseMatchers {

    fun withTextColor(color: Int): Matcher<View> {
        Checks.checkNotNull(color)
        return object : BoundedMatcher<View, TextView>(TextView::class.java) {
            public override fun matchesSafely(textView: TextView): Boolean {
                return color == textView.currentTextColor
            }

            override fun describeTo(description: Description) {
                description.appendText("with text color: ")
            }
        }
    }

    fun isPasswordHidden(): BoundedMatcher<View, EditText> {
        return object : BoundedMatcher<View, EditText>(EditText::class.java) {
            override fun matchesSafely(item: EditText?): Boolean {
                return item?.transformationMethod is PasswordTransformationMethod
            }

            override fun describeTo(description: Description?) {
                description?.appendText("Password is hidden")
            }
        }
    }
}
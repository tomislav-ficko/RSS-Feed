package com.ficko.rssfeed.ui

import android.content.Intent
import android.webkit.WebView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ficko.rssfeed.R
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.matchers.shouldBe
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class WebViewActivityTest : BaseActivityTest() {

    private val item = RssFeedItem().apply {
        name = "item name"
        url = "https://url.com/"
    }

    @Before
    fun setUp() {
        launchActivity<WebViewActivity>(
            Intent().apply { putExtra("rss_feed_item", item) }
        )
    }

    @Test
    fun shouldLoadUrlWhenActivityIsStarted() {
        // Then
        runOnUiThread {
            val loadedUrl = activityInstance.findViewById<WebView>(R.id.web_view).url
            loadedUrl shouldBe item.url
        }
    }

    @Test
    fun shouldDisplayBackButtonAndItemTitleInAppBarWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withText(item.name))).check(matches(isDisplayed()))
        onView(withId(R.id.add_button)).check(matches(not(isDisplayed())))
    }
}
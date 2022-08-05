package com.ficko.rssfeed.ui

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ficko.rssfeed.R
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseFragmentTest
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class RssFeedDetailsFragmentTest : BaseFragmentTest() {

    @BindValue
    @JvmField
    val feedViewModel = mockk<RssFeedViewModel>(relaxed = true)

    private val getRssFeedItemsSuccess = MutableLiveData<List<RssFeedItem>>()

    @Before
    override fun setUp() {
        super.setUp()
        every { feedViewModel.getRssFeedItemsSuccess } returns getRssFeedItemsSuccess
    }

    @Test
    fun shouldGetFeedItemsWhenFragmentIsLoaded() {
        // Given
        val feed = RssFeed()
        val args = Bundle().apply { putSerializable("rssFeed", feed) }

        // When
        loadFragment<RssFeedDetailsFragment>(args)

        // Then
        verify(exactly = 1) { feedViewModel.getRssFeedItems(feed) }
    }

    @Test
    fun shouldDisplayListItemsWhenFeedItemsDataIsReceived() {
        // Given
        loadFragment()
        val itemName = "first feed item"
        val response = listOf(RssFeedItem().apply { name = itemName })

        // When
        feedViewModel.getRssFeedItemsSuccess.postValue(response)

        // Then
        waitForUiThread(300)
        onView(
            allOf(
                isDescendantOfA(withId(R.id.view_holder_container)),
                withText(itemName)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun shouldStartWebViewActivityWhenItemIsClicked() {
        // Given
        loadFragment()
        val itemName = "first feed item"
        val response = listOf(RssFeedItem().apply { name = itemName })
        feedViewModel.getRssFeedItemsSuccess.postValue(response)

        // When
        onView(withId(R.id.view_holder_container)).perform(click())

        // Then
        Intents.intended(hasComponent(WebViewActivity::class.java.name))
    }

    private fun loadFragment() {
        val args = Bundle().apply { putSerializable("rssFeed", RssFeed()) }
        loadFragment<RssFeedDetailsFragment>(args)
    }
}
package com.ficko.rssfeed.ui

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.FlakyTest
import com.ficko.rssfeed.R
import com.ficko.rssfeed.common.TestUtils
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseFragmentTest
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.Event
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@HiltAndroidTest
class RssFeedDetailsFragmentTest : BaseFragmentTest() {

    @BindValue
    @JvmField
    val feedViewModel = mockk<RssFeedViewModel>(relaxed = true)

    @BindValue
    @JvmField
    val appBarViewModel = mockk<AppBarViewModel>(relaxed = true)

    private val getRssFeedItemsSuccess = MutableLiveData<List<RssFeedItem>>()
    private val addFeedToFavoritesSuccess = MutableLiveData<Event<Unit>>()
    private val removeFeedFromFavoritesSuccess = MutableLiveData<Event<Unit>>()
    private val deleteFeedSuccess = MutableLiveData<Unit>()

    @Before
    override fun setUp() {
        super.setUp()
        every { feedViewModel.getRssFeedItemsSuccess } returns getRssFeedItemsSuccess
        every { feedViewModel.addFeedToFavoritesSuccess } returns addFeedToFavoritesSuccess
        every { feedViewModel.removeFeedFromFavoritesSuccess } returns removeFeedFromFavoritesSuccess
        every { feedViewModel.deleteFeedSuccess } returns deleteFeedSuccess
    }

    @Test
    fun shouldUpdateCurrentlyOpenedRssFeedWhenFragmentIsLoaded() {
        // Given
        val feed = RssFeed()
        val args = Bundle().apply { putSerializable("rssFeed", feed) }

        // When
        loadFragment<RssFeedDetailsFragment>(args)

        // Then
        verify(exactly = 1) { feedViewModel.updateCurrentlyOpenedRssFeed(feed) }
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

    @Ignore("Calling navigateUp does not trigger onStop function")
    @Test
    fun shouldUpdateCurrentlyOpenedRssFeedWhenFragmentIsClosing() {
        // Given
        loadFragment()

        // When
        navController.navigateUp()

        // Then
        verify(exactly = 1) { feedViewModel.updateCurrentlyOpenedRssFeed(null) }
    }

    @FlakyTest
    @Test
    fun shouldDisplayAddToFavoritesSuccessMessageWhenLiveDataEventIsReceived() {
        // Given
        loadFragment()

        // When
        feedViewModel.addFeedToFavoritesSuccess.postValue(Event(Unit))

        // Then
        waitForUiThread(300)
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.favorite_added_toast),
            activityInstance
        )
    }

    @FlakyTest
    @Test
    fun shouldDisplayRemoveFromFavoritesSuccessMessageWhenLiveDataEventIsReceived() {
        // Given
        loadFragment()

        // When
        feedViewModel.removeFeedFromFavoritesSuccess.postValue(Event(Unit))

        // Then
        waitForUiThread(300)
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.favorite_removed_toast),
            activityInstance
        )
    }

    @FlakyTest
    @Test
    fun shouldDisplaySuccessNotificationWhenFeedIsSuccessfullyDeleted() {
        // Given
        loadFragment()

        // When
        feedViewModel.deleteFeedSuccess.postValue(Unit)

        // Then
        waitForUiThread(300)
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.delete_feed_success_toast),
            activityInstance
        )
    }

    @Test
    fun shouldNavigateToPreviousScreenWhenFeedIsSuccessfullyDeleted() {
        // Given
        loadFragment()

        // When
        feedViewModel.deleteFeedSuccess.postValue(Unit)

        // Then
        waitForUiThread(300)
        verify(exactly = 1) { navController.navigateUp() }
    }

    @Test
    fun shouldNotifyAppBarViewModelWhenReturningToPreviousScreen() {
        // Given
        loadFragment()

        // When
        feedViewModel.deleteFeedSuccess.postValue(Unit)

        // Then
        verify(exactly = 1) { appBarViewModel.activeFragmentChanged(AppBarViewModel.FragmentType.FEEDS) }
    }

    private fun loadFragment() {
        val args = Bundle().apply { putSerializable("rssFeed", RssFeed()) }
        loadFragment<RssFeedDetailsFragment>(args)
    }
}
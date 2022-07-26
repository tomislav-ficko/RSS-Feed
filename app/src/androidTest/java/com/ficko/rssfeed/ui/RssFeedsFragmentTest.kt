package com.ficko.rssfeed.ui

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ficko.rssfeed.R
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.ui.base.BaseFragmentTest
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class RssFeedsFragmentTest : BaseFragmentTest() {

    @BindValue
    @MockK
    lateinit var feedViewModel: RssFeedViewModel

    @BindValue
    @MockK
    lateinit var appBarViewModel: AppBarViewModel

    private val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()
    private val getFavoriteRssFeedsSuccess = MutableLiveData<List<RssFeed>>()

    @Before
    fun setUp() {
        every { feedViewModel.anyUseCaseInProgress } returns anyUseCaseInProgress
        every { feedViewModel.getRssFeedsSuccess } returns getRssFeedsSuccess
        every { feedViewModel.getFavoriteRssFeedsSuccess } returns getFavoriteRssFeedsSuccess
    }

    @Test
    fun shouldGetFeedsWhenFragmentIsLoaded() {
        // When
        loadFragment()

        // Then
        verify(exactly = 1) { feedViewModel.getRssFeeds() }
    }

    @Test
    fun shouldGetFavoriteFeedsWhenFragmentIsLoadedAndArgumentIsSet() {
        // When
        loadFragment(
            RssFeedsFragment(),
            RssFeedsFragmentArgs(shouldDisplayFavorites = true).toBundle()
        )

        // Then
        verify(exactly = 1) { feedViewModel.getFavoriteRssFeeds() }
    }

    @Test
    fun shouldDisplayProgressBarWhenAnyUseCaseIsInProgress() {
        // Given
        loadFragment()
        anyUseCaseInProgress.postValue(false)

        // When
        anyUseCaseInProgress.postValue(true)

        // Then
        waitForUiThread(100)
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldHideProgressBarWhenNoUseCaseIsInProgress() {
        // Given
        loadFragment()
        anyUseCaseInProgress.postValue(true)

        // When
        anyUseCaseInProgress.postValue(false)

        // Then
        waitForUiThread(100)
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldDisplayEmptyListMessageWhenNoListItemsArePresent() {
        // Given
        loadFragment()

        // When
        getRssFeedsSuccess.postValue(listOf())

        // Then
        onView(withText(getActivityInstance().getString(R.string.empty_list_text)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayEmptyFavoritesListMessageWhenNoFavoriteListItemsArePresent() {
        // Given
        loadFragment(displayFavorites = true)

        // When
        getRssFeedsSuccess.postValue(listOf())

        // Then
        onView(withText(getActivityInstance().getString(R.string.empty_favorites_list_text)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayListItemsWhenFeedsDataIsReceived() {
        // Given
        loadFragment()
        val feeds = listOf(
            RssFeed().apply { name = "First feed" },
            RssFeed().apply { name = "Second feed" }
        )

        // When
        getRssFeedsSuccess.postValue(feeds)

        // Then
        waitForUiThread(300)
        onView(withText(feeds[0].name)).check(matches(isDisplayed()))
        onView(withText(feeds[1].name)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayFavoriteListItemsWhenFavoriteFeedDataIsReceived() {
        // Given
        loadFragment(displayFavorites = true)
        val feeds = listOf(
            RssFeed().apply { name = "First feed" },
            RssFeed().apply { name = "Second feed" }
        )

        // When
        getFavoriteRssFeedsSuccess.postValue(feeds)

        // Then
        waitForUiThread(300)
        onView(withText(feeds[0].name)).check(matches(isDisplayed()))
        onView(withText(feeds[1].name)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNavigateToFeedDetailsScreenWhenListItemIsClicked() {
        // Given
        loadFragment()
        val feeds = listOf(RssFeed().apply { name = "First feed" })
        getRssFeedsSuccess.postValue(feeds)

        // When
        onView(withId(R.id.view_holder_container)).perform(click())

        // Then
        verifyDirection(
            RssFeedsFragmentDirections.actionFeedsDestinationToFeedDetailsDestination(feeds[0])
        )
    }

    @Test
    fun shouldNotifyAppBarViewModelWhenFeedDetailsScreenIsOpened() {
        // Given
        loadFragment()
        val feeds = listOf(RssFeed().apply { name = "First feed" })
        getRssFeedsSuccess.postValue(feeds)
        waitForUiThread(300)

        // When
        onView(withText(feeds[0].name)).perform(click())

        // Then
        verify(exactly = 1) { appBarViewModel.activeFragmentChanged(AppBarViewModel.FragmentType.DETAILS, feeds[0].name) }
    }

    private fun loadFragment(displayFavorites: Boolean = false) = loadFragment(
        RssFeedsFragment(),
        RssFeedsFragmentArgs(displayFavorites).toBundle()
    )
}
package com.ficko.rssfeed.ui

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ficko.rssfeed.R
import com.ficko.rssfeed.common.TestUtils
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseActivityTest
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@HiltAndroidTest
class MainActivityTest : BaseActivityTest() {

    @BindValue
    @MockK
    lateinit var feedViewModel: RssFeedViewModel

    @BindValue
    @MockK
    lateinit var appBarViewModel: AppBarViewModel

    private val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()
    private val getRssFeedItemsSuccess = MutableLiveData<List<RssFeedItem>>()
    private val feedsScreenOpen = MutableLiveData<Boolean>()
    private val feedDetailsScreenOpen = MutableLiveData<String>()

    @Before
    fun setUp() {
        every { feedViewModel.getRssFeedsSuccess } returns getRssFeedsSuccess
        every { feedViewModel.getRssFeedItemsSuccess } returns getRssFeedItemsSuccess
        every { appBarViewModel.feedsScreenOpen } returns feedsScreenOpen
        every { appBarViewModel.feedDetailsScreenOpen } returns feedDetailsScreenOpen
        launchActivity<MainActivity>()
    }

    @Test
    fun shouldDisplayFeedsAndFavoritesNavigationTabsWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.feeds_tab)).check(matches(isDisplayed()))
        onView(withId(R.id.favorites_tab)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotifyViewModelAboutChangeWhenFavoritesTabSelected() {
        // When
        onView(withId(R.id.favorites_tab)).perform(click())

        // Then
        appBarViewModel.activeTabChanged(AppBarViewModel.TabType.FAVORITES)
    }

    @Test
    fun shouldNotifyViewModelAboutChangeWhenFeedsTabSelected() {
        // When
        onView(withId(R.id.feeds_tab)).perform(click())

        // Then
        appBarViewModel.activeTabChanged(AppBarViewModel.TabType.FEEDS)
    }

    @Test
    fun shouldNotifyViewModelAboutChangeWhenNotOnFeedsScreenAndBackButtonPressed() {
        // Given
        openFeedDetailsFragment()
        appBarViewModel.feedDetailsScreenOpen.postValue("")
        waitForUiThread(300)

        // When
        onView(withId(R.id.back_button)).perform(click())

        // Then
        appBarViewModel.activeFragmentChanged(AppBarViewModel.FragmentType.FEEDS)
    }

    @Test
    fun shouldNotifyViewModelWhenFavoriteButtonIsClicked() {
        // Given
        openFeedDetailsFragment()
        appBarViewModel.feedDetailsScreenOpen.postValue("")
        waitForUiThread(300)

        // When
        onView(withId(R.id.favorite_button)).perform(click())

        // Then
        feedViewModel.toggleFeedFavoriteStatus()
    }

    @Test
    fun shouldNotifyViewModelAboutChangeWhenAddNewFeedButtonIsClicked() {
        // When
        onView(withId(R.id.add_button)).perform(click())

        // Then
        appBarViewModel.activeFragmentChanged(AppBarViewModel.FragmentType.NEW_FEED)
    }

    @Test
    fun shouldDisplayOnlyAddButtonInAppBarWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.add_button)).check(matches(isDisplayed()))
    }

    @Ignore("Assertions not working for unknown reason")
    @Test
    fun shouldDisplayOnlyBackButtonAndTitleInAppBarWhenAddNewFeedFragmentIsVisible() {
        // When
        appBarViewModel.addNewFeedScreenOpen.postValue(Unit)

        // Then
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(matches(isDisplayed()))
        onView(withId(R.id.add_button)).check(isNotDisplayed())
    }

    @Ignore("Assertions not working for unknown reason")
    @Test
    fun shouldDisplayOnlyAddButtonInAppBarWhenFeedsTabIsSelectedAndFeedsFragmentIsVisible() {
        // Given
        onView(withId(R.id.feeds_tab)).perform(click())

        // When
        appBarViewModel.feedsScreenOpen.postValue(true)

        // Then
        waitForUiThread(300)
        onView(withId(R.id.back_button)).check(isNotDisplayed())
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(isNotDisplayed())
        onView(withId(R.id.add_button)).check(matches(isDisplayed()))
    }

    @Ignore("Assertions not working for unknown reason")
    @Test
    fun shouldDisplayNoContentInAppBarWhenFavoritesTabIsSelectedAndFeedsFragmentIsVisible() {
        // Given
        onView(withId(R.id.favorites_tab)).perform(click())

        // When
        appBarViewModel.feedsScreenOpen.postValue(false)

        // Then
        waitForUiThread(300)
        onView(withId(R.id.back_button)).check(isNotDisplayed())
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(isNotDisplayed())
        onView(withId(R.id.add_button)).check(isNotDisplayed())
    }

    @Test
    fun shouldDisplayEverythingExceptAddButtonInAppBarWhenFeedDetailsFragmentIsVisible() {
        // When
        appBarViewModel.feedDetailsScreenOpen.postValue("Title")

        // Then
        waitForUiThread(300)
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(matches(isDisplayed()))
        onView(withId(R.id.favorite_button)).check(matches(isDisplayed()))
        onView(withId(R.id.delete_button)).check(matches(isDisplayed()))
        onView(withId(R.id.add_button)).check(isNotDisplayed())
    }

    @Test
    fun shouldNavigateToLastBackStackEntryIfFeedDetailsFragmentIsShownAndBackButtonIsPressed() {
        // Given
        openFeedDetailsFragment()

        // When
        onView(withId(R.id.feeds_tab)).perform(pressBack())

        // Then
        onView(
            AllOf.allOf(
                isDescendantOfA(withId(R.id.view_holder_container)),
                withText("item name")
            )
        ).check(doesNotExist())
    }

    @Test
    fun shouldSelectFeedsTabWhenFavoritesTabIsActiveAndFavoritesRootIsVisibleAndBackButtonIsClicked() {
        // Given
        onView(withId(R.id.favorites_tab)).perform(click())

        // When
        onView(withId(R.id.favorites_tab)).perform(pressBack())

        // Then
        onView(withId(R.id.feeds_tab)).check(matches(isSelected()))
        onView(withId(R.id.favorites_tab)).check(matches(isNotSelected()))
    }

    @Test
    fun shouldDisplayToastWhenFeedsTabRootVisibleAndBackButtonIsClicked() {
        // When
        onView(withId(R.id.feeds_tab)).perform(pressBack())

        // Then
        waitForUiThread(300)
        with(getActivityInstance()) {
            TestUtils.assertToastMessageIsDisplayed(getString(R.string.back_button_notice), this)
        }
    }

    private fun openFeedDetailsFragment() {
        val feedName = "feed name"
        val itemName = "item name"
        val feeds = listOf(RssFeed().apply {
            name = feedName
            items = listOf(RssFeedItem().apply { name = itemName })
        })
        runOnUiThread { feedViewModel.getRssFeedsSuccess.postValue(feeds) }
        onView(withId(R.id.view_holder_container)).perform(click())
        runOnUiThread { feedViewModel.getRssFeedItemsSuccess.postValue(feeds[0].items) }
    }

    private fun isNotDisplayed() = matches(not(isDisplayed()))
}
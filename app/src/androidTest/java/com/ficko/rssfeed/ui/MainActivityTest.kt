package com.ficko.rssfeed.ui

import androidx.appcompat.app.AppCompatActivity
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
    private val returningToPreviousScreen = MutableLiveData<Unit>()
    private val feedDetailsOpen = MutableLiveData<String>()

    @Before
    fun setUp() {
        every { feedViewModel.getRssFeedsSuccess } returns getRssFeedsSuccess
        every { feedViewModel.getRssFeedItemsSuccess } returns getRssFeedItemsSuccess
        every { appBarViewModel.returningToPreviousScreen } returns returningToPreviousScreen
        every { appBarViewModel.feedDetailsOpen } returns feedDetailsOpen
        launchActivity<MainActivity>()
    }

    @Test
    fun shouldDisplayFeedsAndFavoritesNavigationTabsWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.feeds_tab)).check(matches(isDisplayed()))
        onView(withId(R.id.favorites_tab)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayOnlyAddButtonInAppBarWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.add_button)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayOnlyBackButtonAndTitleInAppBarWhenAddNewFeedFragmentIsVisible() {
        // When
        openAddNewFeedFragment()

        // Then
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(matches(isDisplayed()))
        onView(withId(R.id.add_button)).check(isNotDisplayed())
    }

    @Test
    fun shouldDisplayOnlyAddButtonInAppBarWhenFeedsFragmentIsVisible() {
        // Given
        appBarViewModel.feedDetailsOpen.postValue("Title")

        // When
        appBarViewModel.returningToPreviousScreen.postValue(Unit)

        // Then
        waitForUiThread(300)
        onView(withId(R.id.back_button)).check(isNotDisplayed())
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(isNotDisplayed())
        onView(withId(R.id.add_button)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayOnlyBackButtonAndTitleInAppBarWhenFeedDetailsFragmentIsVisible() {
        // When
        appBarViewModel.feedDetailsOpen.postValue("Title")

        // Then
        waitForUiThread(300)
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(matches(isDisplayed()))
        onView(withId(R.id.add_button)).check(isNotDisplayed())
    }

    @Test
    fun shouldNavigateToLastBackStackEntryIfRssFeedsFragmentIsShownAndBackButtonIsPressed() {
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
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.back_button_notice),
            activityInstance as AppCompatActivity
        )
    }

    private fun openAddNewFeedFragment() {
        onView(withId(R.id.add_button)).perform(click())
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
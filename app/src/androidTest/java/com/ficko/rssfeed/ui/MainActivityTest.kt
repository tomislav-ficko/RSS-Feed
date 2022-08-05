package com.ficko.rssfeed.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ficko.rssfeed.R
import com.ficko.rssfeed.ui.base.BaseActivityTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class MainActivityTest : BaseActivityTest() {

    @Before
    fun setUp() {
        launchActivity<MainActivity>()
    }

    @Test
    fun shouldDisplayAddButtonInAppBarWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.add_button)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayFeedsAndFavoritesNavigationTabsWhenActivityIsStarted() {
        // Then
        onView(withId(R.id.feeds_tab)).check(matches(isDisplayed()))
        onView(withId(R.id.favorites_tab)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayBackButtonAndTitleInAppBarAndHideAddButtonWhenAddButtonIsClicked() {
        // When
        onView(withId(R.id.add_button)).perform(click())

        // Then
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(allOf(isDescendantOfA(withId(R.id.app_bar)), withId(R.id.title))).check(matches(isDisplayed()))
        onView(withId(R.id.add_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldOpenAddRssFeedFragmentWhenAddButtonIsClicked() {
        // TODO
    }

    @Test
    fun shouldNavigateToLastBackStackEntryIfRssFeedsFragmentIsShownAndBackButtonIsPressed() {
        // TODO
    }
}
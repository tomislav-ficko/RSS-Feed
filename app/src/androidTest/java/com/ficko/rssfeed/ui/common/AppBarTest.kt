package com.ficko.rssfeed.ui.common

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ficko.rssfeed.R
import com.ficko.rssfeed.ui.base.BaseViewTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class AppBarTest : BaseViewTest() {

    @MockK
    private lateinit var listener: AppBar.AppBarListener

    private lateinit var appBar: AppBar

    @Before
    override fun setUp() {
        super.setUp()
        runOnUiThread {
            appBar = AppBar(getActivityInstance())
            loadView(appBar)
        }
    }

    @Test
    fun shouldShowBackButtonWhenEnabled() {
        // When
        appBar.updateView(backButtonEnabled = true)

        // Then
        waitForUiThread(200)
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowTitleWhenProvided() {
        // Given
        val titleValue = "title"

        // When
        appBar.updateView(title = titleValue)

        // Then
        waitForUiThread(200)
        onView(withText(titleValue)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowAddButtonWhenEnabled() {
        // When
        appBar.updateView(addButtonEnabled = true)

        // Then
        waitForUiThread(200)
        onView(withId(R.id.add_button)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowFavoriteButtonWhenEnabled() {
        // When
        appBar.updateView(favoriteButtonEnabled = true)

        // Then
        waitForUiThread(200)
        onView(withId(R.id.favorite_button)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldFireCallbackWhenBackButtonIsClickedAndListenerIsSet() {
        // Given
        appBar.setListener(listener)
        appBar.updateView(backButtonEnabled = true)
        waitForUiThread(200)

        // When
        onView(withId(R.id.back_button)).perform(click())

        // Then
        verify(exactly = 1) { listener.backButtonClicked() }
    }

    @Test
    fun shouldFireCallbackWhenAddButtonIsClickedAndListenerIsSet() {
        // Given
        appBar.setListener(listener)
        appBar.updateView(addButtonEnabled = true)
        waitForUiThread(200)

        // When
        onView(withId(R.id.add_button)).perform(click())

        // Then
        verify(exactly = 1) { listener.addButtonClicked() }
    }

    @Test
    fun shouldFireCallbackWhenFavoriteButtonIsClickedAndListenerIsSet() {
        // Given
        appBar.setListener(listener)
        appBar.updateView(favoriteButtonEnabled = true)
        waitForUiThread(200)

        // When
        onView(withId(R.id.favorite_button)).perform(click())

        // Then
        verify(exactly = 1) { listener.favoriteButtonClicked() }
    }
}
package com.ficko.rssfeed.ui

import android.view.KeyEvent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.FlakyTest
import com.ficko.rssfeed.R
import com.ficko.rssfeed.common.TestUtils
import com.ficko.rssfeed.ui.base.BaseFragmentTest
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class NewFeedFragmentTest : BaseFragmentTest() {

    @BindValue
    @JvmField
    val feedViewModel = mockk<RssFeedViewModel>(relaxed = true)

    @BindValue
    @JvmField
    val appBarViewModel = mockk<AppBarViewModel>(relaxed = true)

    private val addNewFeedSuccess = MutableLiveData<Unit>()
    private val feedExists = MutableLiveData<Unit>()

    @Before
    override fun setUp() {
        super.setUp()
        every { feedViewModel.addNewFeedSuccess } returns addNewFeedSuccess
        every { feedViewModel.feedExists } returns feedExists
        every { feedViewModel.anyUseCaseFailure } returns anyUseCaseFailed
        loadFragment<NewFeedFragment>()
    }

    @Test
    fun shouldInitiateAddingNewFeedWhenUrlIsEnteredAndButtonIsClicked() {
        // Given
        val url = "http://"
        onView(withId(R.id.input)).perform(click())
        onView(withId(R.id.input)).perform(replaceText(url))

        // When
        onView(withId(R.id.button)).perform(click())

        // Then
        verify(exactly = 1) { feedViewModel.addNewFeed(url) }
    }

    @Test
    fun shouldInitiateAddingNewFeedWhenUrlIsEnteredAndKeyboardEnterButtonIsClicked() {
        // Given
        val url = "http://"
        onView(withId(R.id.input)).perform(click())
        onView(withId(R.id.input)).perform(replaceText(url))

        // When
        onView(withId(R.id.input)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        // Then
        verify(exactly = 1) { feedViewModel.addNewFeed(url) }
    }

    @FlakyTest
    @Test
    fun shouldDisplaySuccessNotificationWhenFeedIsSuccessfullyAdded() {
        // When
        feedViewModel.addNewFeedSuccess.postValue(Unit)

        // Then
        waitForUiThread(300)
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.new_feed_success_toast),
            activityInstance
        )
    }

    @Test
    fun shouldNavigateToPreviousScreenWhenFeedIsSuccessfullyAdded() {
        // When
        feedViewModel.addNewFeedSuccess.postValue(Unit)

        // Then
        waitForUiThread(300)
        verify(exactly = 1) { navController.navigateUp() }
    }

    @FlakyTest
    @Test
    fun shouldDisplayToastWhenFeedAlreadyExists() {
        // When
        feedViewModel.feedExists.postValue(Unit)

        // Then
        waitForUiThread(300)
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.new_feed_exists_toast),
            activityInstance
        )
    }

    @Test
    fun shouldNavigateToPreviousScreenWhenFeedAlreadyExists() {
        // When
        feedViewModel.feedExists.postValue(Unit)

        // Then
        waitForUiThread(300)
        verify(exactly = 1) { navController.navigateUp() }
    }

    @FlakyTest
    @Test
    fun shouldDisplayToastWhenErrorOccursWhileAddingFeed() {
        // When
        feedViewModel.anyUseCaseFailure.postValue(Exception())

        // Then
        waitForUiThread(300)
        TestUtils.assertToastMessageIsDisplayed(
            activityInstance.getString(R.string.new_feed_failure_toast),
            activityInstance
        )
    }

    @FlakyTest
    @Test
    fun shouldNotifyAppBarViewModelWhenReturningToPreviousScreen() {
        // When
        feedViewModel.addNewFeedSuccess.postValue(Unit)

        // Then
        verify(exactly = 1) { appBarViewModel.returningToPreviousScreen() }
    }
}
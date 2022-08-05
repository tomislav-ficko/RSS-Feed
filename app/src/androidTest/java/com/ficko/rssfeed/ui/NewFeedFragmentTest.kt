package com.ficko.rssfeed.ui

import androidx.lifecycle.MutableLiveData
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

    @Before
    override fun setUp() {
        super.setUp()
        every { feedViewModel.addNewFeedSuccess } returns addNewFeedSuccess
        loadFragment<NewFeedFragment>()
    }

    @Test
    fun shouldDisplaySuccessNotificationWhenAddressIsSuccessfullyAdded() {
        // Given
        val toastMessage = "Successfully subscribed to RSS feed"

        // When
        feedViewModel.addNewFeedSuccess.postValue(Unit)

        // Then
        TestUtils.assertToastMessageIsDisplayed(toastMessage, activityInstance)
    }

    @Test
    fun shouldNavigateToPreviousScreenWhenAddressIsSuccessfullyAdded() {
        // When
        feedViewModel.addNewFeedSuccess.postValue(Unit)

        // Then
        waitForUiThread(300)
        verify(exactly = 1) { navController.navigateUp() }
    }

    @Test
    fun shouldNotifyAppBarViewModelWhenReturningToPreviousScreen() {
        // When
        feedViewModel.addNewFeedSuccess.postValue(Unit)

        // Then
        verify(exactly = 1) { appBarViewModel.returningToPreviousScreen() }
    }
}
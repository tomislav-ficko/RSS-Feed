package com.ficko.rssfeed.vm

import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Test

class AppBarViewModelTest : BaseViewModelTest() {

    @InjectMockKs
    private lateinit var viewModel: AppBarViewModel

    @Test
    fun shouldTriggerLiveDataWhenActiveTabIsChangedToFavoritesAndCurrentFragmentIsFeeds() {
        // When
        viewModel.activeTabChanged(AppBarViewModel.TabType.FAVORITES)

        // Then
        viewModel.feedsScreenOpen.value shouldBe false
    }

    @Test
    fun shouldTriggerLiveDataWhenActiveTabIsChangedToFeedsAndCurrentFragmentIsFeeds() {
        // Given
        viewModel.activeTabChanged(AppBarViewModel.TabType.FAVORITES)

        // When
        viewModel.activeTabChanged(AppBarViewModel.TabType.FEEDS)

        // Then
        viewModel.feedsScreenOpen.value shouldBe true
    }

    @Test
    fun shouldTriggerLiveDataAndReturnPreviousDetailsTitleWhenReturningToPreviousTab() {
        // Given
        val feedName = "name"
        viewModel.activeFragmentChanged(AppBarViewModel.FragmentType.DETAILS, feedName)
        viewModel.activeTabChanged(AppBarViewModel.TabType.FAVORITES)

        // When
        viewModel.activeTabChanged(AppBarViewModel.TabType.FEEDS)

        // Then
        viewModel.feedDetailsScreenOpen.value shouldBe feedName
    }

    @Test
    fun shouldTriggerLiveDataWhenFeedsScreenBecomesActive() {
        // When
        viewModel.activeFragmentChanged(AppBarViewModel.FragmentType.FEEDS)

        // Then
        viewModel.feedsScreenOpen.value shouldBe true
    }

    @Test
    fun shouldTriggerLiveDataWhenAddNewFeedScreenBecomesActive() {
        // When
        viewModel.activeFragmentChanged(AppBarViewModel.FragmentType.NEW_FEED)

        // Then
        viewModel.addNewFeedScreenOpen.value shouldBe Unit
    }
}
package com.ficko.rssfeed.vm

import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Test

class AppBarViewModelTest : BaseViewModelTest() {

    @InjectMockKs
    private lateinit var viewModel: AppBarViewModel

    @Test
    fun shouldNotifyAboutDetailsScreenOpened() {
        // Given
        val feedName = "name"

        // When
        viewModel.feedDetailsScreenOpened(feedName)

        // Then
        viewModel.feedDetailsOpen.value shouldBe feedName
    }

    @Test
    fun shouldNotifyAboutReturningToPreviousScreen() {
        // When
        viewModel.returningToPreviousScreen()

        // Then
        viewModel.returningToPreviousScreen.value shouldBe Unit
    }
}
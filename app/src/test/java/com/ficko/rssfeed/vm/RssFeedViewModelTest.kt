package com.ficko.rssfeed.vm

import com.ficko.rssfeed.data.RssFeedRepository
import com.ficko.rssfeed.data.local.preferences.PreferenceHandler
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Test

class RssFeedViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var repository: RssFeedRepository

    @InjectMockKs
    private lateinit var viewModel: RssFeedViewModel

    @Test
    fun shouldGetRssFeeds() {
        // Given
        val feeds = listOf(RssFeed().apply { id = "1" })
        coEvery { repository.updateRssFeeds() } returns Unit
        coEvery { repository.getRssFeeds() } returns feeds

        // When
        viewModel.getRssFeeds()

        // Then
        coVerify(exactly = 1) { repository.updateRssFeeds() }
        coVerify(exactly = 1) { repository.getRssFeeds() }
        viewModel.getRssFeedsSuccess.value shouldBe feeds
    }

    @Test
    fun shouldGetFavoriteRssFeeds() {
        // Given
        val feed = RssFeed().apply { rssUrl = "url" }
        coEvery { repository.getRssFeeds() } returns listOf(feed)
        PreferenceHandler.putFavoriteFeedUrls(setOf(feed.rssUrl))

        // When
        viewModel.getFavoriteRssFeeds()

        // Then
        viewModel.getFavoriteRssFeedsSuccess.value?.size shouldBe 1
        viewModel.getFavoriteRssFeedsSuccess.value?.first() shouldBe feed
    }

    @Test
    fun shouldGetRssFeedItems() {
        // Given
        val feed = RssFeed().apply {
            items = listOf(RssFeedItem())
        }

        // When
        viewModel.getRssFeedItems(feed)

        // Then
        viewModel.getRssFeedItemsSuccess.value shouldBe feed.items
    }

    @Test
    fun shouldAddNewRssFeedWhenFeedWithSameRssUrlDoesNotAlreadyExistInDatabase() {
        // Given
        val url = "rss_url"
        coEvery { repository.getRssFeeds() } returns listOf()
        coEvery { repository.addNewFeed(url) } returns Unit

        // When
        viewModel.addNewFeed(url)

        // Then
        coVerify(exactly = 1) { repository.getRssFeeds() }
        coVerify(exactly = 1) { repository.addNewFeed(url) }
        viewModel.addNewFeedSuccess.value shouldBe Unit
    }

    @Test
    fun shouldPostLiveDataWhenFeedWithSameRssUrlAlreadyExistsInDatabase() {
        // Given
        val url = "rss_url"
        val existingFeed = RssFeed().apply { rssUrl = url }
        coEvery { repository.getRssFeeds() } returns listOf(existingFeed)

        // When
        viewModel.addNewFeed(url)

        // Then
        coVerify(exactly = 1) { repository.getRssFeeds() }
        viewModel.feedExists.value shouldBe Unit
    }

    @Test
    fun shouldAddFeedToFavorites() {
        // Given
        val feed = RssFeed().apply { rssUrl = "url" }

        // When
        viewModel.addFeedToFavorites(feed)

        // Then
        viewModel.addFeedToFavoritesSuccess.value shouldBe Unit
        PreferenceHandler.getFavoriteFeedUrls().size shouldBe 1
        PreferenceHandler.getFavoriteFeedUrls().first() shouldBe feed.rssUrl
    }

    @Test
    fun shouldRemoveFeedFromFavorites() {
        // Given
        val feed = RssFeed().apply { rssUrl = "url" }
        PreferenceHandler.putFavoriteFeedUrls(setOf(feed.rssUrl))

        // When
        viewModel.removeFeedFromFavorites(feed)

        // Then
        viewModel.removeFeedFromFavoritesSuccess.value shouldBe Unit
        PreferenceHandler.getFavoriteFeedUrls().size shouldBe 0
    }
}
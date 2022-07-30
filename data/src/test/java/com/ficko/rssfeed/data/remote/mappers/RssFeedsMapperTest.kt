package com.ficko.rssfeed.data.remote.mappers

import com.ficko.rssfeed.data.remote.responses.RssFeedItemResponse
import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import io.kotest.matchers.shouldBe
import org.junit.Test


class RssFeedsMapperTest {

    @Test
    fun shouldMapRssFeedsResponseToRssFeeds() {
        // Given
        val input = listOf(
            RssFeedResponse().apply {
                title = "feed title"
                description = "feed description"
                link = "https://rss-feed.url"
                imageUrl = "https://rss-feed-image.url"
                items = listOf(
                    RssFeedItemResponse().apply {
                        title = "item title"
                        description = "item description"
                        link = "https://item.url"
                        imageUrl = "https://item-image.url"
                    }
                )
            }
        )

        // When
        val result = RssFeedsMapper.mapRssFeedResponsesToRssFeeds(input)

        // Then
        with(result[0]) {
            input[0].let {
                name shouldBe it.title
                description shouldBe it.description
                url shouldBe it.link
                imageUrl shouldBe it.imageUrl
                items[0].name shouldBe it.items!![0].title
                items[0].description shouldBe it.items!![0].description
                items[0].url shouldBe it.items!![0].link
                items[0].imageUrl shouldBe it.items!![0].imageUrl
            }
        }
    }
}
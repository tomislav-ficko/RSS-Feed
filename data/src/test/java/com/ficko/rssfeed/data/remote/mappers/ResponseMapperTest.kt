package com.ficko.rssfeed.data.remote.mappers

import com.ficko.rssfeed.data.remote.responses.RssFeedItemResponse
import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import io.kotest.matchers.shouldBe
import org.junit.Test

class ResponseMapperTest {

    @Test
    fun shouldMapRssFeedResponseToRssFeed() {
        // Given
        val input = RssFeedResponse().apply {
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

        // When
        val result = ResponseMapper.mapRssFeedResponseToRssFeed(input)

        // Then
        with(result) {
            input.let {
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
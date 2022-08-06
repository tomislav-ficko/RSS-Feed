package com.ficko.rssfeed.data.local.database.mappers

import com.ficko.rssfeed.data.local.database.dto.RssFeedDto
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import io.kotest.matchers.shouldBe
import org.junit.Test


class RssFeedDtoMapperTest {

    @Test
    fun shouldMapRssFeedToDto() {
        // Given
        val feed = RssFeed().apply {
            id = "feed_id"
            rssUrl = "rss_url"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "item_id" })
        }

        // When
        val feedDto = RssFeedDtoMapper.mapRssFeedToDto(feed)

        // Then
        feedDto.id shouldBe feed.id
        feedDto.rssUrl shouldBe feed.rssUrl
        feedDto.url shouldBe feed.url
        feedDto.name shouldBe feed.name
        feedDto.description shouldBe feed.description
        feedDto.imageUrl shouldBe feed.imageUrl
        feedDto.items[0].id shouldBe feed.items[0].id
    }

    @Test
    fun shouldMapDtoToRssFeed() {
        // Given
        val feedDto = RssFeedDto().apply {
            id = "feed_id"
            rssUrl = "rss_url"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "item_id" })
        }

        // When
        val feed = RssFeedDtoMapper.mapDtoToRssFeed(feedDto)

        // Then
        feedDto.id shouldBe feed.id
        feedDto.rssUrl shouldBe feed.rssUrl
        feedDto.url shouldBe feed.url
        feedDto.name shouldBe feed.name
        feedDto.description shouldBe feed.description
        feedDto.imageUrl shouldBe feed.imageUrl
        feedDto.items[0].id shouldBe feed.items[0].id
    }

    @Test
    fun shouldMapRssFeedsToDtoList() {
        // Given
        val feedOne = RssFeed().apply { id = "feed_id" }
        val feedTwo = RssFeed().apply { id = "feed_id" }
        val feeds = listOf(feedOne, feedTwo)

        // When
        val dtoList = RssFeedDtoMapper.mapRssFeedsToDtoList(feeds)

        // Then
        dtoList[0].id shouldBe feedOne.id
        dtoList[1].id shouldBe feedTwo.id
    }

    @Test
    fun shouldMapDtoListToRssFeeds() {
        // Given
        val feedDtoOne = RssFeedDto().apply { id = "feed_id" }
        val feedDtoTwo = RssFeedDto().apply { id = "feed_id" }
        val dtoList = listOf(feedDtoOne, feedDtoTwo)

        // When
        val feeds = RssFeedDtoMapper.mapDtoListToRssFeeds(dtoList)

        // Then
        feeds[0].id shouldBe feedDtoOne.id
        feeds[1].id shouldBe feedDtoTwo.id
    }
}
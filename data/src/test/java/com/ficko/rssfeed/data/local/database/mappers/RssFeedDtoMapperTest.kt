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
        feedDto.url shouldBe feed.url
        feedDto.name shouldBe feed.name
        feedDto.description shouldBe feed.description
        feedDto.imageUrl shouldBe feed.imageUrl
        feedDto.items[0].id shouldBe feed.items[0].id
    }

    @Test
    fun shouldMapRssFeedsToDtoList() {
        // Given
        val feedOne = RssFeed().apply {
            id = "feed_id"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "item_id" })
        }
        val feedTwo = RssFeed().apply {
            id = "feed_id"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "item_id" })
        }
        val feeds = listOf(feedOne, feedTwo)

        // When
        val dtoList = RssFeedDtoMapper.mapRssFeedsToDtoList(feeds)

        // Then
        with(dtoList[0]) {
            id shouldBe feedOne.id
            url shouldBe feedOne.url
            name shouldBe feedOne.name
            description shouldBe feedOne.description
            imageUrl shouldBe feedOne.imageUrl
            items[0].id shouldBe feedOne.items[0].id
        }
        with(dtoList[1]) {
            id shouldBe feedTwo.id
            url shouldBe feedTwo.url
            name shouldBe feedTwo.name
            description shouldBe feedTwo.description
            imageUrl shouldBe feedTwo.imageUrl
            items[0].id shouldBe feedTwo.items[0].id
        }
    }

    @Test
    fun shouldMapDtoListToRssFeeds() {
        // Given
        val feedDtoOne = RssFeedDto().apply {
            id = "feed_id"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "item_id" })
        }
        val feedDtoTwo = RssFeedDto().apply {
            id = "feed_id"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "item_id" })
        }
        val dtoList = listOf(feedDtoOne, feedDtoTwo)

        // When
        val feeds = RssFeedDtoMapper.mapDtoListToRssFeeds(dtoList)

        // Then
        with(feeds[0]) {
            id shouldBe feedDtoOne.id
            url shouldBe feedDtoOne.url
            name shouldBe feedDtoOne.name
            description shouldBe feedDtoOne.description
            imageUrl shouldBe feedDtoOne.imageUrl
            items[0].id shouldBe feedDtoOne.items[0].id
        }
        with(feeds[1]) {
            id shouldBe feedDtoTwo.id
            url shouldBe feedDtoTwo.url
            name shouldBe feedDtoTwo.name
            description shouldBe feedDtoTwo.description
            imageUrl shouldBe feedDtoTwo.imageUrl
            items[0].id shouldBe feedDtoTwo.items[0].id
        }
    }
}
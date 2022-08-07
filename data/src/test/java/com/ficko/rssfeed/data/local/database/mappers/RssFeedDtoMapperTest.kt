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
            id = "1"
            rssUrl = "rss_url"
            url = "url"
            name = "name"
            description = "description"
            imageUrl = "image_url"
            items = listOf(RssFeedItem().apply { id = "11" })
        }

        // When
        val feedDto = RssFeedDtoMapper.mapRssFeedToDto(feed)

        // Then
        feedDto.id shouldBe feed.id.toInt()
        feedDto.rssUrl shouldBe feed.rssUrl
        feedDto.url shouldBe feed.url
        feedDto.name shouldBe feed.name
        feedDto.description shouldBe feed.description
        feedDto.imageUrl shouldBe feed.imageUrl
        feedDto.items!![0].id shouldBe feed.items[0].id
    }

    @Test
    fun shouldMapDtoToRssFeed() {
        // Given
        val feedDto = getSampleFeedDto(1)

        // When
        val feed = RssFeedDtoMapper.mapDtoToRssFeed(feedDto)

        // Then
        feed.id shouldBe feedDto.id.toString()
        feed.rssUrl shouldBe feedDto.rssUrl
        feed.url shouldBe feedDto.url
        feed.name shouldBe feedDto.name
        feed.description shouldBe feedDto.description
        feed.imageUrl shouldBe feedDto.imageUrl
        feed.items[0].id shouldBe feedDto.items!![0].id
    }

    @Test
    fun shouldMapRssFeedsToDtoList() {
        // Given
        val feedOne = RssFeed().apply { id = "0" }
        val feedTwo = RssFeed().apply { id = "1" }
        val feeds = listOf(feedOne, feedTwo)

        // When
        val dtoList = RssFeedDtoMapper.mapRssFeedsToDtoList(feeds)

        // Then
        dtoList[0].id shouldBe feedOne.id.toInt()
        dtoList[1].id shouldBe feedTwo.id.toInt()
    }

    @Test
    fun shouldMapDtoListToRssFeeds() {
        // Given
        val feedDtoOne = getSampleFeedDto(0)
        val feedDtoTwo = getSampleFeedDto(1)
        val dtoList = listOf(feedDtoOne, feedDtoTwo)

        // When
        val feeds = RssFeedDtoMapper.mapDtoListToRssFeeds(dtoList)

        // Then
        feeds[0].id shouldBe feedDtoOne.id.toString()
        feeds[1].id shouldBe feedDtoTwo.id.toString()
    }

    private fun getSampleFeedDto(id: Int): RssFeedDto {
        return RssFeedDto(
            id = id,
            rssUrl = "rss_url",
            url = "url",
            name = "name",
            description = "description",
            imageUrl = "image_url",
            items = listOf(RssFeedItem().apply { this.id = "11" })
        )
    }
}
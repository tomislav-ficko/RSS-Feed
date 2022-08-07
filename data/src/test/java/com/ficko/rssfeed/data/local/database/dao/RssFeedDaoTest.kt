package com.ficko.rssfeed.data.local.database.dao

import com.ficko.rssfeed.data.local.database.dto.RssFeedDto
import com.ficko.rssfeed.domain.RssFeedItem
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RssFeedDaoTest : BaseDaoTest() {

    @Test
    fun shouldInsertNewRssFeedByIgnoringNewValueIfOldValueIsPresent() = runBlocking {
        // Given
        val dto = getSampleFeedDto(1)

        // When
        testDatabase.rssFeedDao().insert(dto)

        // Then
        val dtoFromDb = testDatabase.rssFeedDao().getAll()[0]

        dtoFromDb.id shouldBe dto.id
    }

    @Test
    fun shouldGetRssFeedBasedOnId() = runBlocking {
        // Given
        val dto = getSampleFeedDto(1)
        testDatabase.rssFeedDao().insert(dto)

        // When
        val dtoFromDb = testDatabase.rssFeedDao().get(dto.id)

        // Then
        dtoFromDb.id shouldBe dto.id
    }

    @Test
    fun shouldGetAllRssFeeds() = runBlocking {
        // Given
        val dtoOne = getSampleFeedDto(1)
        val dtoTwo = getSampleFeedDto(2)
        testDatabase.rssFeedDao().insert(dtoOne, dtoTwo)

        // When
        val dtoListFromDb = testDatabase.rssFeedDao().getAll()

        // Then
        dtoListFromDb[0].id shouldBe dtoOne.id
        dtoListFromDb[1].id shouldBe dtoTwo.id
    }

    @Test
    fun shouldDeleteRssFeed() = runBlocking {
        // Given
        val dtoOne = getSampleFeedDto(1)
        val dtoTwo = getSampleFeedDto(2)
        testDatabase.rssFeedDao().insert(dtoOne, dtoTwo)

        // When
        testDatabase.rssFeedDao().delete(dtoOne)

        // Then
        val dtoListFromDb = testDatabase.rssFeedDao().getAll()
        dtoListFromDb.size shouldBe 1
        dtoListFromDb[0].id shouldBe dtoTwo.id
    }

    @Test
    fun shouldDeleteRssFeedBasedOnId() = runBlocking {
        // Given
        val dtoOne = getSampleFeedDto(1)
        val dtoTwo = getSampleFeedDto(2)
        testDatabase.rssFeedDao().insert(dtoOne, dtoTwo)

        // When
        testDatabase.rssFeedDao().delete(dtoOne.id)

        // Then
        val dtoListFromDb = testDatabase.rssFeedDao().getAll()
        dtoListFromDb.size shouldBe 1
        dtoListFromDb[0].id shouldBe dtoTwo.id
    }

    @Test
    fun shouldDeleteAllRssFeeds() = runBlocking {
        // Given
        val dto = getSampleFeedDto(1)
        testDatabase.rssFeedDao().insert(dto)

        // When
        testDatabase.rssFeedDao().deleteAll()

        // Then
        val dtoListFromDb = testDatabase.rssFeedDao().getAll()
        dtoListFromDb shouldBe emptyList()
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
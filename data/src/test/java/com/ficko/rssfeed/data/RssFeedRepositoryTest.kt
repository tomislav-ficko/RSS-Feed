package com.ficko.rssfeed.data

import com.ficko.rssfeed.data.local.database.dao.RssFeedDao
import com.ficko.rssfeed.data.local.database.dto.RssFeedDto
import com.ficko.rssfeed.data.local.database.mappers.DtoMapper
import com.ficko.rssfeed.data.remote.apis.RssFeedApi
import com.ficko.rssfeed.data.remote.responses.RssFeedItemResponse
import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import com.ficko.rssfeed.domain.RssFeedItem
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RssFeedRepositoryTest {

    @MockK
    private lateinit var dao: RssFeedDao

    @MockK
    private lateinit var api: RssFeedApi

    @InjectMockKs
    private lateinit var repository: RssFeedRepository

    @Before
    fun setupTest() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun shouldGetRssFeeds() = runBlocking {
        // Given
        val dto = getSampleFeedDto()
        val dtoList = listOf(dto)
        val expectedResult = DtoMapper.mapDtoToRssFeed(dto)
        coEvery { dao.getAll() } returns dtoList

        // When
        val actualResult = repository.getRssFeeds()

        // Then
        coVerify(exactly = 1) { dao.getAll() }
        actualResult[0].id shouldBe expectedResult.id
        actualResult[0].rssUrl shouldBe expectedResult.rssUrl
    }

    @Test
    fun shouldUpdateRssFeeds() = runBlocking {
        // Given
        val feedItem = RssFeedItemResponse().apply { title = "new_item" }
        val expectedApiResponse = RssFeedResponse().apply { items = listOf(feedItem) }
        val dto = getSampleFeedDto()
        val dtoList = listOf(dto)
        coEvery { dao.getAll() } returns dtoList
        coEvery { api.getRssFeed(dto.rssUrl) } returns expectedApiResponse

        // When
        repository.updateRssFeeds()

        // Then
        coVerify(exactly = 1) { dao.getAll() }
        coVerify(exactly = 1) { api.getRssFeed(dto.rssUrl) }
        coVerify(exactly = 1) { dao.deleteAll() }
        coVerify(exactly = 1) { dao.insert(any()) }
    }

    @Test
    fun shouldAddNewRssFeed() = runBlocking {
        // Given
        val url = "url"
        val expectedApiResponse = RssFeedResponse()
        coEvery { api.getRssFeed(url) } returns expectedApiResponse

        // When
        repository.addNewFeed(url)

        // Then
        coVerify(exactly = 1) { api.getRssFeed(url) }
        val dtoSlot = slot<RssFeedDto>()
        coVerify(exactly = 1) { dao.insert(capture(dtoSlot)) }
        dtoSlot.captured.rssUrl shouldBe url
    }

    private fun getSampleFeedDto(): RssFeedDto {
        return RssFeedDto(
            id = 1,
            rssUrl = "rss_url",
            url = "url",
            name = "name",
            description = "description",
            imageUrl = "image_url",
            items = listOf(RssFeedItem().apply { this.id = "11" })
        )
    }
}
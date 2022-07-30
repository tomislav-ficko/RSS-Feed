package com.ficko.rssfeed.data.remote.interfaces

import com.ficko.rssfeed.data.remote.apis.RssFeedApi
import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class RssFeedApiTest : BaseApiTest() {

    private lateinit var rssFeedApi: RssFeedApi

    @Before
    fun setUp() {
        rssFeedApi = testRetrofit.create(RssFeedApi::class.java)
    }

    @Ignore("Not working until a way is found to manually serialize object to XML")
    @Test
    fun shouldGetRssFeed() = runBlocking {
        // Given
        val url = "https://www.rssfeed.com"
        val expectedResponse = RssFeedResponse().apply { title = "name" }
        enqueueResponse(expectedResponse)

        // When
        val actualResponse = rssFeedApi.getRssFeed(url)

        // Then
        takeRequest().requestUrl shouldBe url
        actualResponse.title shouldBe expectedResponse.title
    }
}
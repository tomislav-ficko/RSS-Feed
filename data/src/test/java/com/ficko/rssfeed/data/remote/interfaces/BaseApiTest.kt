package com.ficko.rssfeed.data.remote.interfaces

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.net.HttpURLConnection

abstract class BaseApiTest {

    protected lateinit var testRetrofit: Retrofit
    private val mockWebServer = MockWebServer()

    @Before
    fun setUpServer() {
        mockWebServer.start()
        testRetrofit = Retrofit.Builder()
            .baseUrl("https://placeholder.url")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    @After
    fun tearDownServer() {
        mockWebServer.shutdown()
    }

    protected fun enqueueResponse(body: Any) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
//                .setBody(body) TODO body must be serialized to XML before being passed into the function
        )
    }

    protected fun takeRequest() = mockWebServer.takeRequest()
}
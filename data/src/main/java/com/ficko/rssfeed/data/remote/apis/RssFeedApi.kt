package com.ficko.rssfeed.data.remote.apis

import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface RssFeedApi {

    @GET
    suspend fun getRssFeed(@Url url: String): RssFeedResponse
}
package com.ficko.rssfeed.data.remote.mappers

import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem

object ResponseMapper {

    fun mapRssFeedResponseToRssFeed(response: RssFeedResponse): RssFeed {
        return RssFeed().apply {
            response.title?.let { name = it }
            response.description?.let { description = it }
            response.link?.let { url = it }
            response.imageUrl?.let { imageUrl = it }
            response.items?.let { itemResponses ->
                items = itemResponses.map { itemResponse ->
                    RssFeedItem().apply {
                        itemResponse.title?.let { name = it }
                        itemResponse.description?.let { description = it }
                        itemResponse.link?.let { url = it }
                        itemResponse.imageUrl?.let { imageUrl = it }
                    }
                }
            }
        }
    }
}
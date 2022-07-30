package com.ficko.rssfeed.data.remote.mappers

import com.ficko.rssfeed.data.remote.responses.RssFeedResponse
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem

object RssFeedsMapper {

    fun mapRssFeedResponsesToRssFeeds(response: List<RssFeedResponse>): List<RssFeed> {
        return response.map { feedResponse ->
            RssFeed().apply {
                feedResponse.title?.let { name = it }
                feedResponse.description?.let { description = it }
                feedResponse.link?.let { url = it }
                feedResponse.imageUrl?.let { imageUrl = it }
                feedResponse.items?.let { itemResponses ->
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
}
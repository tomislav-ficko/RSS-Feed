package com.ficko.rssfeed.data.local.database.mappers

import com.ficko.rssfeed.data.local.database.dto.RssFeedDto
import com.ficko.rssfeed.domain.RssFeed

object RssFeedDtoMapper {

    fun mapRssFeedToDto(rssFeed: RssFeed): RssFeedDto {
        return RssFeedDto().apply {
            id = rssFeed.id
            url = rssFeed.url
            name = rssFeed.name
            description = rssFeed.description
            imageUrl = rssFeed.imageUrl
            items = rssFeed.items
        }
    }

    fun mapDtoToRssFeed(dto: RssFeedDto): RssFeed {
        return RssFeed().apply {
            id = dto.id
            url = dto.url
            name = dto.name
            description = dto.description
            imageUrl = dto.imageUrl
            items = dto.items
        }
    }

    fun mapRssFeedsToDtoList(rssFeeds: List<RssFeed>): List<RssFeedDto> {
        return rssFeeds.map { feed -> mapRssFeedToDto(feed) }
    }

    fun mapDtoListToRssFeeds(dtoList: List<RssFeedDto>): List<RssFeed> {
        return dtoList.map { dto -> mapDtoToRssFeed(dto) }
    }
}
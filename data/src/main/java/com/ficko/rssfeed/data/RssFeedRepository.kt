package com.ficko.rssfeed.data

import com.ficko.rssfeed.data.local.database.dao.RssFeedDao
import com.ficko.rssfeed.data.local.database.mappers.RssFeedDtoMapper
import com.ficko.rssfeed.data.remote.apis.RssFeedApi
import com.ficko.rssfeed.data.remote.mappers.RssFeedsMapper
import com.ficko.rssfeed.domain.RssFeed
import javax.inject.Inject

class RssFeedRepository @Inject constructor(
    private val dao: RssFeedDao,
    private val api: RssFeedApi
) {

    suspend fun getRssFeeds(): List<RssFeed> {
        return RssFeedDtoMapper.mapDtoListToRssFeeds(dao.getAll())
    }

    suspend fun updateRssFeeds() {
        val currentFeeds = RssFeedDtoMapper.mapDtoListToRssFeeds(dao.getAll())
        val updatedFeeds = getUpdatedFeedData(currentFeeds)
        insertIntoDb(updatedFeeds)
    }

    suspend fun addNewFeed(rssUrl: String) {
        val dto = RssFeedDtoMapper.mapRssFeedToDto(
            RssFeed().apply { this.rssUrl = rssUrl }
        )
        dao.insert(dto)
    }

    private suspend fun getUpdatedFeedData(currentFeeds: List<RssFeed>): List<RssFeed> {
        return currentFeeds.map {
            val response = api.getRssFeed(it.rssUrl)
            RssFeedsMapper.mapRssFeedResponseToRssFeed(response).apply { rssUrl = it.rssUrl }
        }
    }

    private suspend fun insertIntoDb(models: List<RssFeed>) {
        val dtoList = RssFeedDtoMapper.mapRssFeedsToDtoList(models).toTypedArray()
        dao.deleteAll()
        dao.insert(*dtoList)
    }
}
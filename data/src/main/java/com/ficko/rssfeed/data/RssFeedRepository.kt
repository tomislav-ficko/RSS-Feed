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
        val updatedFeeds = currentFeeds.map {
            val response = api.getRssFeed(it.url)
            RssFeedsMapper.mapRssFeedResponseToRssFeed(response)
        }
        insertIntoDb(updatedFeeds)
    }

    suspend fun addNewFeed(url: String) {
        val dto = RssFeedDtoMapper.mapRssFeedToDto(
            RssFeed().apply { this.url = url }
        )
        dao.insert(dto)
    }

    private suspend fun insertIntoDb(models: List<RssFeed>) {
        val dtoList = RssFeedDtoMapper.mapRssFeedsToDtoList(models).toTypedArray()
        dao.insert(*dtoList)
    }
}
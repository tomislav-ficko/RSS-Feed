package com.ficko.rssfeed.data

import com.ficko.rssfeed.data.local.database.dao.RssFeedDao
import com.ficko.rssfeed.data.local.database.mappers.DtoMapper
import com.ficko.rssfeed.data.remote.apis.RssFeedApi
import com.ficko.rssfeed.data.remote.mappers.ResponseMapper
import com.ficko.rssfeed.domain.RssFeed
import javax.inject.Inject

class RssFeedRepository @Inject constructor(
    private val dao: RssFeedDao,
    private val api: RssFeedApi
) {

    suspend fun getRssFeeds(): List<RssFeed> {
        return DtoMapper.mapDtoListToRssFeeds(dao.getAll())
    }

    suspend fun updateRssFeeds() {
        val currentFeeds = DtoMapper.mapDtoListToRssFeeds(dao.getAll())
        val updatedFeeds = getUpdatedFeedData(currentFeeds)
        insertIntoDb(updatedFeeds)
    }

    suspend fun addNewFeed(rssUrl: String) {
        val response = api.getRssFeed(rssUrl)
        val model = ResponseMapper.mapRssFeedResponseToRssFeed(response).apply { this.rssUrl = rssUrl }
        val dto = DtoMapper.mapRssFeedToDto(model)
        dao.insert(dto)
    }

    private suspend fun getUpdatedFeedData(currentFeeds: List<RssFeed>): List<RssFeed> {
        return currentFeeds.map {
            val response = api.getRssFeed(it.rssUrl)
            ResponseMapper.mapRssFeedResponseToRssFeed(response).apply { rssUrl = it.rssUrl }
        }
    }

    private suspend fun insertIntoDb(models: List<RssFeed>) {
        val dtoList = DtoMapper.mapRssFeedsToDtoList(models).toTypedArray()
        dao.deleteAll()
        dao.insert(*dtoList)
    }
}
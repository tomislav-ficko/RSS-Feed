package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import com.ficko.rssfeed.data.RssFeedRepository
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RssFeedViewModel @Inject constructor(
    private val repository: RssFeedRepository
) : BaseViewModel() {

    val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()
    val getRssFeedItemsSuccess = MutableLiveData<List<RssFeedItem>>()
    val addNewFeedSuccess = MutableLiveData<Unit>()
    val feedExists = MutableLiveData<Unit>()

    fun getRssFeeds() {
        executeUseCase {
            repository.updateRssFeeds()
            val response = repository.getRssFeeds()
            getRssFeedsSuccess.postValue(response)
        }
    }

    fun getRssFeedItems(feed: RssFeed) {
        getRssFeedItemsSuccess.postValue(feed.items)
    }

    fun addNewFeed(url: String) {
        executeUseCase {
            val feedAlreadyExists = repository.getRssFeeds().any { it.rssUrl == url }
            if (feedAlreadyExists)
                feedExists.postValue(Unit)
            else {
                repository.addNewFeed(url)
                addNewFeedSuccess.postValue(Unit)
            }
        }
    }
}
package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RssFeedViewModel @Inject constructor() : BaseViewModel() {

    val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()
    val getRssFeedItemsSuccess = MutableLiveData<List<RssFeedItem>>()

    fun getRssFeeds() {
        getRssFeedsSuccess.postValue(getSampleFeeds())
    }

    fun getRssFeedItems(feed: RssFeed) {
        getRssFeedItemsSuccess.postValue(getSampleFeedItems())
    }

    private fun getSampleFeeds() = List(6) {
        RssFeed().apply {
            name = "Feed"
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been..."
            url = "https://feed.com"
            imageUrl = "https://picsum.photos/200"
            items = getSampleFeedItems()
        }
    }

    private fun getSampleFeedItems() = List(6) {
        RssFeedItem().apply {
            name = "Item"
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been..."
            url = "https://item.com"
            imageUrl = "https://picsum.photos/200"
        }
    }
}
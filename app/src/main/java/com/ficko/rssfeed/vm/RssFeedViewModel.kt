package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RssFeedViewModel @Inject constructor() : BaseViewModel() {

    val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()

    fun getRssFeeds() {
        getRssFeedsSuccess.postValue(listOf(
            RssFeed().apply {
                name = "Feed One"
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been..."
                url = "https://feed.one.com"
                imageUrl = "https://picsum.photos/200"
                items = getSampleFeedItems()
            },
            RssFeed().apply {
                name = "Feed Two"
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been..."
                url = "https://feed.two.com"
                imageUrl = "https://picsum.photos/200"
                items = getSampleFeedItems()
            }
        ))
    }

    private fun getSampleFeedItems() = listOf(
        RssFeedItem().apply {
            name = "Item One"
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been..."
            url = "https://item.one.com"
            imageUrl = "https://picsum.photos/200"
        },
        RssFeedItem().apply {
            name = "Item Two"
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been..."
            url = "https://item.two.com"
            imageUrl = "https://picsum.photos/200"
        }
    )
}
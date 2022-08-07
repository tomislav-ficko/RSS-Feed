package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import com.ficko.rssfeed.data.RssFeedRepository
import com.ficko.rssfeed.data.local.preferences.PreferenceHandler
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.domain.RssFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RssFeedViewModel @Inject constructor(
    private val repository: RssFeedRepository
) : BaseViewModel() {

    val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()
    val getFavoriteRssFeedsSuccess = MutableLiveData<List<RssFeed>>()
    val getRssFeedItemsSuccess = MutableLiveData<List<RssFeedItem>>()
    val addNewFeedSuccess = MutableLiveData<Unit>()
    val addFeedToFavoritesSuccess = MutableLiveData<Unit>()
    val removeFeedFromFavoritesSuccess = MutableLiveData<Unit>()
    val feedExists = MutableLiveData<Unit>()

    fun getRssFeeds() {
        executeUseCase {
            repository.updateRssFeeds()
            val response = repository.getRssFeeds()
            getRssFeedsSuccess.postValue(response)
        }
    }

    fun getFavoriteRssFeeds() {
        executeUseCase {
            val favoriteUrls = PreferenceHandler.getFavoriteFeedUrls()
            val favoriteFeeds = repository.getRssFeeds().filter { favoriteUrls.contains(it.rssUrl) }
            getFavoriteRssFeedsSuccess.postValue(favoriteFeeds)
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

    fun addFeedToFavorites(feed: RssFeed) {
        val favorites = PreferenceHandler.getFavoriteFeedUrls()
        PreferenceHandler.putFavoriteFeedUrls(favorites + feed.rssUrl)
        addFeedToFavoritesSuccess.postValue(Unit)
    }

    fun removeFeedFromFavorites(feed: RssFeed) {
        val favorites = PreferenceHandler.getFavoriteFeedUrls().toMutableSet()
        favorites.remove(feed.rssUrl)
        PreferenceHandler.putFavoriteFeedUrls(favorites)
        removeFeedFromFavoritesSuccess.postValue(Unit)
    }
}
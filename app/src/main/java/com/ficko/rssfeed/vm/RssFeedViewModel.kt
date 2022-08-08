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
    val addFeedToFavoritesSuccess = MutableLiveData<Event<Unit>>()
    val removeFeedFromFavoritesSuccess = MutableLiveData<Event<Unit>>()
    val feedExists = MutableLiveData<Unit>()

    private var currentlyOpenedRssFeed: RssFeed? = null

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

    fun toggleFeedFavoriteStatus() {
        currentlyOpenedRssFeed?.let { feed ->
            val favorites = PreferenceHandler.getFavoriteFeedUrls()
            if (favorites.contains(feed.rssUrl)) removeFeedFromFavorites()
            else addFeedToFavorites()
        }
    }

    private fun addFeedToFavorites() {
        val favorites = PreferenceHandler.getFavoriteFeedUrls()
        PreferenceHandler.putFavoriteFeedUrls(favorites + currentlyOpenedRssFeed!!.rssUrl)
        addFeedToFavoritesSuccess.postValue(Event(Unit))
    }

    private fun removeFeedFromFavorites() {
        val favorites = PreferenceHandler.getFavoriteFeedUrls().toMutableSet()
        favorites.remove(currentlyOpenedRssFeed!!.rssUrl)
        PreferenceHandler.putFavoriteFeedUrls(favorites)
        removeFeedFromFavoritesSuccess.postValue(Event(Unit))
    }

    fun updateCurrentlyOpenedRssFeed(rssFeed: RssFeed?) {
        currentlyOpenedRssFeed = rssFeed
    }
}
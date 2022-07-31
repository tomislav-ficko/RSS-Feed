package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : BaseViewModel() {

    private enum class Screen {
        FEEDS, FEED_DETAILS, NEW_FEED, WEB_VIEW
    }

    val newFeedOpen = MutableLiveData<Unit>()
    val feedDetailsOpen = MutableLiveData<String>()
    val webViewOpen = MutableLiveData<String>()
    val feedsOpen = MutableLiveData<Unit>()

    private var currentScreen = Screen.FEEDS
    private var lastFeedName = ""

    fun addNewFeedOpened() {
        currentScreen = Screen.NEW_FEED
        newFeedOpen.postValue(Unit)
    }

    fun feedDetailsOpened(feedName: String) {
        currentScreen = Screen.FEED_DETAILS
        lastFeedName = feedName
        feedDetailsOpen.postValue(feedName)
    }

    fun webViewOpened(itemName: String) {
        currentScreen = Screen.WEB_VIEW
        webViewOpen.postValue(itemName)
    }

    fun returningToPreviousScreen() {
        when (currentScreen) {
            Screen.FEEDS -> {}
            Screen.FEED_DETAILS, Screen.NEW_FEED -> {
                currentScreen = Screen.FEEDS
                feedsOpen.postValue(Unit)
            }
            Screen.WEB_VIEW -> {
                currentScreen = Screen.FEED_DETAILS
                feedDetailsOpen.postValue(lastFeedName)
            }
        }
    }
}
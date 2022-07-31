package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : BaseViewModel() {

    private enum class Screen {
        FEEDS, FEED_DETAILS, NEW_FEED, WEB_VIEW
    }

    val feedDetailsOpen = MutableLiveData<String>()
    val feedsOpen = MutableLiveData<Unit>()

    private var currentScreen = Screen.FEEDS

    fun feedDetailsOpened(feedName: String) {
        currentScreen = Screen.FEED_DETAILS
        feedDetailsOpen.postValue(feedName)
    }

    fun returningToPreviousScreen() {
        when (currentScreen) {
            Screen.FEEDS -> {}
            Screen.FEED_DETAILS, Screen.NEW_FEED -> {
                currentScreen = Screen.FEEDS
                feedsOpen.postValue(Unit)
            }
            Screen.WEB_VIEW -> TODO()
        }
    }
}
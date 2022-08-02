package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor() : BaseViewModel() {

    val feedDetailsOpen = MutableLiveData<String>()
    val returningToPreviousScreen = MutableLiveData<Unit>()

    fun feedDetailsScreenOpened(feedName: String) {
        feedDetailsOpen.postValue(feedName)
    }

    fun returningToPreviousScreen() {
        returningToPreviousScreen.postValue(Unit)
    }
}
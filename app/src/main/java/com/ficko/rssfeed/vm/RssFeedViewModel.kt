package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import com.ficko.rssfeed.domain.RssFeed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RssFeedViewModel @Inject constructor() : BaseViewModel() {

    val getRssFeedsSuccess = MutableLiveData<List<RssFeed>>()

    fun getRssFeeds() {}
}
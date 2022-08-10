package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import com.ficko.rssfeed.domain.RssFeed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor() : BaseViewModel() {

    enum class TabType { FEEDS, FAVORITES }
    enum class FragmentType { FEEDS, DETAILS, NEW_FEED }

    val feedsScreenOpen = MutableLiveData<Boolean>()
    val feedDetailsScreenOpen = MutableLiveData<String>()
    val addNewFeedScreenOpen = MutableLiveData<Unit>()

    private var activeTab = TabType.FEEDS
    private var activeFragmentOnFeedsTab = FragmentType.FEEDS
    private var activeFragmentOnFavoritesTab = FragmentType.FEEDS
    private var lastDetailsScreenTitleForFeedsTab = ""
    private var lastDetailsScreenTitleForFavoritesTab = ""

    fun activeTabChanged(newActiveTab: TabType) {
        activeTab = newActiveTab
        notifyAboutChangeToAppBar()
    }

    fun activeFragmentChanged(
        newActiveFragment: FragmentType,
        detailsScreenTitle: String? = null
    ) {
        if (activeTab == TabType.FEEDS)
            activeFragmentOnFeedsTab = newActiveFragment
        else
            activeFragmentOnFavoritesTab = newActiveFragment
        saveNewTitleForDetailsScreen(detailsScreenTitle)
        notifyAboutChangeToAppBar()
    }

    fun updateAppBarOfOtherTabIfFeedDetailsWereOpen(deletedFeed: RssFeed) {
        if (onFeedsTabAndFavoritesTabWasDisplayingThisFeedsDetails(deletedFeed))
            activeFragmentOnFavoritesTab = FragmentType.FEEDS
        else if (onFavoritesTabAndFeedsTabWasDisplayingThisFeedsDetails(deletedFeed))
            activeFragmentOnFeedsTab = FragmentType.FEEDS
    }

    private fun saveNewTitleForDetailsScreen(detailsScreenTitle: String?) {
        if (activeFragmentOnFeedsTab == FragmentType.DETAILS) {
            detailsScreenTitle?.let {
                when (activeTab) {
                    TabType.FEEDS -> lastDetailsScreenTitleForFeedsTab = it
                    TabType.FAVORITES -> lastDetailsScreenTitleForFavoritesTab = it
                }
            }
        }
    }

    private fun notifyAboutChangeToAppBar() {
        if (activeTab == TabType.FEEDS) {
            when (activeFragmentOnFeedsTab) {
                FragmentType.FEEDS -> feedsScreenOpen.postValue(true)
                FragmentType.DETAILS -> feedDetailsScreenOpen.postValue(lastDetailsScreenTitleForFeedsTab)
                FragmentType.NEW_FEED -> addNewFeedScreenOpen.postValue(Unit)
            }
        } else {
            when (activeFragmentOnFavoritesTab) {
                FragmentType.FEEDS -> feedsScreenOpen.postValue(false)
                FragmentType.DETAILS -> feedDetailsScreenOpen.postValue(lastDetailsScreenTitleForFavoritesTab)
                FragmentType.NEW_FEED -> addNewFeedScreenOpen.postValue(Unit)
            }
        }
    }

    private fun onFeedsTabAndFavoritesTabWasDisplayingThisFeedsDetails(deletedFeed: RssFeed) = activeTab == TabType.FEEDS
        && activeFragmentOnFavoritesTab == FragmentType.DETAILS
        && lastDetailsScreenTitleForFavoritesTab == deletedFeed.name

    private fun onFavoritesTabAndFeedsTabWasDisplayingThisFeedsDetails(deletedFeed: RssFeed) = activeTab == TabType.FAVORITES
        && activeFragmentOnFeedsTab == FragmentType.DETAILS
        && lastDetailsScreenTitleForFeedsTab == deletedFeed.name
}
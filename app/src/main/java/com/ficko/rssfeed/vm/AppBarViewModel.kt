package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor() : BaseViewModel() {

    enum class TabType { FEEDS, FAVORITES }
    enum class FragmentType { FEEDS, DETAILS, NEW_FEED }

    val feedsScreenOpen = MutableLiveData<Unit>()
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
                FragmentType.FEEDS -> feedsScreenOpen.postValue(Unit)
                FragmentType.DETAILS -> feedDetailsScreenOpen.postValue(lastDetailsScreenTitleForFeedsTab)
                FragmentType.NEW_FEED -> addNewFeedScreenOpen.postValue(Unit)
            }
        } else {
            when (activeFragmentOnFavoritesTab) {
                FragmentType.FEEDS -> feedsScreenOpen.postValue(Unit)
                FragmentType.DETAILS -> feedDetailsScreenOpen.postValue(lastDetailsScreenTitleForFavoritesTab)
                FragmentType.NEW_FEED -> addNewFeedScreenOpen.postValue(Unit)
            }
        }
    }
}
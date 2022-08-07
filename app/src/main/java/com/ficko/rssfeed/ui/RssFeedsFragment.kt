package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.RssFeedsFragmentBinding
import com.ficko.rssfeed.domain.CommonRssAttributes
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RssFeedsFragment : BaseFragment<RssFeedsFragmentBinding>(R.layout.rss_feeds_fragment),
    ListAdapter.ListViewHolderListener {

    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val appBarViewModel by activityViewModels<AppBarViewModel>()
    private lateinit var adapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        feedViewModel.getRssFeeds()
    }

    override fun itemClicked(item: CommonRssAttributes) {
        appBarViewModel.feedDetailsScreenOpened(item.name)
        RssFeedsFragmentDirections.actionFeedsDestinationToFeedDetailsDestination(item as RssFeed).execute()
    }

    private fun observeViewModel() {
        feedViewModel.getRssFeedsSuccess.observe(requireActivity()) { setUpFragment(it) }
        feedViewModel.anyUseCaseInProgress.observe(requireActivity()) { binding.progressBarVisible = it }
    }

    private fun setUpFragment(feeds: List<RssFeed>) {
        adapter = ListAdapter(feeds).apply { setListener(this@RssFeedsFragment) }
        binding.recyclerView.adapter = adapter
        binding.messageVisible = feeds.isEmpty()
    }
}
package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.RssFeedsFragmentBinding
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.vm.NavigationViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RssFeedsFragment : BaseFragment<RssFeedsFragmentBinding>(R.layout.rss_feeds_fragment),
    RssFeedAdapter.RssFeedViewHolderListener {

    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val navigationViewModel by viewModels<NavigationViewModel>()
    private lateinit var adapter: RssFeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        feedViewModel.getRssFeeds()
    }

    override fun itemClicked(item: RssFeed) {
        navigationViewModel.feedDetailsOpened(item.name)
        RssFeedsFragmentDirections.actionFeedsDestinationToFeedDetailsDestination(item)
    }

    private fun observeViewModel() {
        feedViewModel.getRssFeedsSuccess.observe(requireActivity()) { setUpFragment(it) }
    }

    private fun setUpFragment(feeds: List<RssFeed>) {
        adapter = RssFeedAdapter(feeds).apply { setListener(this@RssFeedsFragment) }
        binding.recyclerView.adapter = adapter
    }
}
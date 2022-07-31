package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.RssFeedDetailsFragmentBinding
import com.ficko.rssfeed.domain.CommonRssAttributes
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.vm.NavigationViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RssFeedDetailsFragment : BaseFragment<RssFeedDetailsFragmentBinding>(R.layout.rss_feed_details_fragment),
    ListAdapter.ListViewHolderListener {

    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val navigationViewModel by viewModels<NavigationViewModel>()
    private val args by navArgs<RssFeedDetailsFragmentArgs>()
    private lateinit var adapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        feedViewModel.getRssFeedItems(args.rssFeed)
    }

    override fun itemClicked(item: CommonRssAttributes) {
        // TODO open WebViewActivity
    }

    private fun observeViewModel() {
        feedViewModel.getRssFeedItemsSuccess.observe(requireActivity()) { setUpFragment(it) }
    }

    private fun setUpFragment(items: List<RssFeedItem>) {
        adapter = ListAdapter(items).apply { setListener(this@RssFeedDetailsFragment) }
        binding.recyclerView.adapter = adapter
    }
}
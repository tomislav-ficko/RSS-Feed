package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.RssFeedsFragmentBinding
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RssFeedDetailsFragment : BaseFragment<RssFeedsFragmentBinding>(R.layout.rss_feed_details_fragment),
    RssFeedItemAdapter.ListViewHolderListener {

    private val viewModel by viewModels<RssFeedViewModel>()
    private val args by navArgs<RssFeedDetailsFragmentArgs>()
    private lateinit var adapter: RssFeedItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.getRssFeedItems(args.rssFeed)
    }

    override fun itemClicked(item: RssFeedItem) {
        // TODO open WebViewActivity
    }

    private fun observeViewModel() {
        viewModel.getRssFeedItemsSuccess.observe(requireActivity()) { setUpFragment(it) }
    }

    private fun setUpFragment(items: List<RssFeedItem>) {
        adapter = RssFeedItemAdapter(items).apply { setListener(this@RssFeedDetailsFragment) }
        binding.recyclerView.adapter = adapter
    }
}
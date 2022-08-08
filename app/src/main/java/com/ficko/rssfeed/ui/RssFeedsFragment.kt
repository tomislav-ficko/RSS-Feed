package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.RssFeedsFragmentBinding
import com.ficko.rssfeed.domain.CommonRssAttributes
import com.ficko.rssfeed.domain.RssFeed
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.AppBarViewModel.FragmentType
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RssFeedsFragment : BaseFragment<RssFeedsFragmentBinding>(R.layout.rss_feeds_fragment),
    ListAdapter.ListViewHolderListener {

    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val appBarViewModel by activityViewModels<AppBarViewModel>()
    private val args by navArgs<RssFeedsFragmentArgs>()
    private lateinit var adapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (args.shouldDisplayFavorites) feedViewModel.getFavoriteRssFeeds()
        else feedViewModel.getRssFeeds()
    }

    override fun itemClicked(item: CommonRssAttributes) {
        appBarViewModel.activeFragmentChanged(FragmentType.DETAILS, item.name)
        RssFeedsFragmentDirections.actionFeedsDestinationToFeedDetailsDestination(item as RssFeed).execute()
    }

    private fun observeViewModel() {
        feedViewModel.getRssFeedsSuccess.observe(viewLifecycleOwner) { setUpFragment(it) }
        feedViewModel.getFavoriteRssFeedsSuccess.observe(viewLifecycleOwner) { setUpFragment(it) }
        feedViewModel.anyUseCaseInProgress.observe(viewLifecycleOwner) { binding.progressBarVisible = it }
    }

    private fun setUpFragment(feeds: List<RssFeed>) {
        adapter = ListAdapter(feeds).apply { setListener(this@RssFeedsFragment) }
        binding.apply {
            recyclerView.adapter = adapter
            emptyListText.text = getString(
                if (args.shouldDisplayFavorites) R.string.empty_favorites_list_text
                else R.string.empty_list_text
            )
            messageVisible = feeds.isEmpty()
        }
    }
}
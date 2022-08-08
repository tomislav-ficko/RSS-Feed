package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.RssFeedDetailsFragmentBinding
import com.ficko.rssfeed.domain.CommonRssAttributes
import com.ficko.rssfeed.domain.RssFeedItem
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.ui.common.Utils
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RssFeedDetailsFragment : BaseFragment<RssFeedDetailsFragmentBinding>(R.layout.rss_feed_details_fragment),
    ListAdapter.ListViewHolderListener {

    private val feedViewModel by activityViewModels<RssFeedViewModel>()
    private val appBarViewModel by activityViewModels<AppBarViewModel>()
    private val args by navArgs<RssFeedDetailsFragmentArgs>()
    private lateinit var adapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        feedViewModel.updateCurrentlyOpenedRssFeed(args.rssFeed)
        feedViewModel.getRssFeedItems(args.rssFeed)
    }

    override fun onStop() {
        super.onStop()
        feedViewModel.updateCurrentlyOpenedRssFeed(null)
    }

    override fun itemClicked(item: CommonRssAttributes) {
        startActivity(
            WebViewActivity.buildIntent(requireContext(), item as RssFeedItem)
        )
    }

    private fun observeViewModel() {
        feedViewModel.getRssFeedItemsSuccess.observe(viewLifecycleOwner) { setUpFragment(it) }
        feedViewModel.addFeedToFavoritesSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                showSuccessNotification(requireContext().getString(R.string.favorite_added_toast))
            }
        }
        feedViewModel.removeFeedFromFavoritesSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                showSuccessNotification(requireContext().getString(R.string.favorite_removed_toast))
            }
        }
        feedViewModel.deleteFeedSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                showSuccessNotification(requireContext().getString(R.string.delete_feed_success_toast))
                navigateToPreviousFragment()
            }
        }
    }

    private fun setUpFragment(items: List<RssFeedItem>) {
        adapter = ListAdapter(items).apply { setListener(this@RssFeedDetailsFragment) }
        binding.recyclerView.adapter = adapter
    }

    private fun showSuccessNotification(message: String) {
        try {
            Utils.showSuccessNotification(requireContext(), layoutInflater, message)
        } catch (e: IllegalStateException) {
            Timber.d(e)
        }
    }

    private fun navigateToPreviousFragment() {
        appBarViewModel.activeFragmentChanged(AppBarViewModel.FragmentType.FEEDS)
        findNavController().navigateUp()
    }
}
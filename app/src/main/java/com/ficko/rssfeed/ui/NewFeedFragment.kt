package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.NewFeedFragmentBinding
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.ui.common.Utils
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewFeedFragment : BaseFragment<NewFeedFragmentBinding>(R.layout.new_feed_fragment),
    TextView.OnEditorActionListener {

    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val appBarViewModel by activityViewModels<AppBarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpFragment()
    }

    override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return event?.let {
            if (it.action == KeyEvent.ACTION_DOWN && it.keyCode == KeyEvent.KEYCODE_ENTER) buttonClicked()
            true
        } ?: false
    }

    fun buttonClicked() {
        Utils.closeSoftKeyboard(requireActivity())
        val input = binding.input.text.trim().toString()
        if (input.isNotEmpty())
            feedViewModel.addNewFeed(input)
    }

    private fun observeViewModel() {
        feedViewModel.addNewFeedSuccess.observe(viewLifecycleOwner) {
            val message = requireContext().getString(R.string.new_feed_success_toast)
            Utils.showSuccessNotification(requireContext(), layoutInflater, message)
            navigateToPreviousFragment()
        }
        feedViewModel.feedExists.observe(viewLifecycleOwner) {
            Utils.showToast(requireActivity(), R.string.new_feed_exists_toast)
            navigateToPreviousFragment()
        }
        feedViewModel.anyUseCaseFailure.observe(viewLifecycleOwner) {
            Utils.showToast(requireActivity(), R.string.new_feed_failure_toast)
        }
    }

    private fun setUpFragment() {
        binding.fragment = this
        binding.input.setOnEditorActionListener(this)
    }

    private fun navigateToPreviousFragment() {
        appBarViewModel.returningToPreviousScreen()
        findNavController().navigateUp()
    }
}
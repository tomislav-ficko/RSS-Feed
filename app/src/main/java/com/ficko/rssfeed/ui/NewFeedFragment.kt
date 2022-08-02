package com.ficko.rssfeed.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.NewFeedFragmentBinding
import com.ficko.rssfeed.ui.base.BaseFragment
import com.ficko.rssfeed.ui.common.Utils
import com.ficko.rssfeed.vm.NavigationViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewFeedFragment : BaseFragment<NewFeedFragmentBinding>(R.layout.new_feed_fragment) {

    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val navigationViewModel by viewModels<NavigationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpFragment()
    }

    fun buttonClicked() {
        Utils.closeSoftKeyboard(requireActivity())
        val input = binding.input.text.trim().toString()
        if (input.isNotEmpty())
            feedViewModel.addNewFeed(input)
    }

    private fun observeViewModel() {
        feedViewModel.addNewFeedSuccess.observe(viewLifecycleOwner) {
            val message = requireContext().getString(R.string.success_toast)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            navigateToPreviousFragmentAfterDelay()
        }
    }

    private fun setUpFragment() {
        binding.fragment = this
    }

    private fun navigateToPreviousFragmentAfterDelay() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            navigationViewModel.returningToPreviousScreen()
            findNavController().navigateUp()
        }
    }
}
package com.ficko.rssfeed.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.MainActivityBinding
import com.ficko.rssfeed.ui.base.BaseActivity
import com.ficko.rssfeed.ui.common.AppBar
import com.ficko.rssfeed.ui.common.Utils
import com.ficko.rssfeed.vm.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity(),
    AppBar.AppBarListener {

    private val navigationViewModel by viewModels<NavigationViewModel>()
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val feedsNavController by lazy { findNavController(binding.feedsContainer) }
    private val favoritesNavController by lazy { findNavController(binding.favoritesContainer) }
    private var appClosable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        setUpActivity()
    }

    override fun onBackPressed() {
        navigationViewModel.returningToPreviousScreen()
        if (favoritesTabSelected() && !favoritesRootVisible()) {
            favoritesNavController.navigateUp()
        } else if (favoritesTabSelected() && favoritesRootVisible()) {
            selectFeedsTab()
        } else if (feedsTabSelected() && !feedsRootVisible()) {
            feedsNavController.navigateUp()
        } else if (feedsTabSelected() && !appClosable) {
            appClosable = true
            Utils.showToast(this, R.string.back_button_notice)
            enableAppCloseNoticeAfterDelay()
        } else {
            super.onBackPressed()
        }
    }

    override fun addButtonClicked() {
        showScreenForAddingNewFeed()
    }

    override fun backButtonClicked() {
        onBackPressed()
    }

    private fun observeViewModel() {
        navigationViewModel.apply {
            feedsOpen.observe(this@MainActivity) {
                binding.appBar.updateView(
                    backButtonEnabled = false,
                    title = "",
                    addButtonEnabled = true
                )
            }
            feedDetailsOpen.observe(this@MainActivity) { feedName ->
                binding.appBar.updateView(
                    backButtonEnabled = true,
                    title = feedName,
                    addButtonEnabled = false
                )
            }
            newFeedOpen.observe(this@MainActivity) {
                binding.appBar.updateView(
                    backButtonEnabled = true,
                    addButtonEnabled = false,
                    title = getString(R.string.title_add_new_feed)
                )
            }
        }
    }

    private fun setUpActivity() {
        binding.appBar.setListener(this)
        setUpBottomNavBar()
    }

    private fun setUpBottomNavBar() {
        binding.activeTabIndex = 0
        binding.bottomNavBar.setOnNavigationItemSelectedListener { selectedTab ->
            updateActiveTabIndex(selectedTab.itemId)
            true
        }
        setUpTabColors()
    }

    private fun showScreenForAddingNewFeed() {
        navigationViewModel.addNewFeedOpened()
        RssFeedsFragmentDirections.actionFeedsDestinationToNewFeedFragment().execute()
    }

    private fun feedsTabSelected() = binding.activeTabIndex == 0
    private fun favoritesTabSelected() = binding.activeTabIndex == 1
    private fun feedsRootVisible() = feedsNavController.currentDestination?.id == R.id.feeds_destination
    private fun favoritesRootVisible() = favoritesNavController.currentDestination?.id == R.id.favorites_destination

    private fun selectFeedsTab() {
        binding.bottomNavBar.selectedItemId = R.id.feeds_tab
        binding.activeTabIndex = 0
    }

    private fun updateActiveTabIndex(activeItemId: Int) {
        binding.activeTabIndex = if (activeItemId == R.id.feeds_tab) 0 else 1
    }

    private fun setUpTabColors() {
        val stateUnchecked = intArrayOf(-android.R.attr.state_checked)
        val stateChecked = intArrayOf(android.R.attr.state_checked)
        val colorUnchecked = ContextCompat.getColor(this, R.color.grey)
        val colorChecked = ContextCompat.getColor(this, R.color.navigation_blue)
        val states = arrayOf(stateUnchecked, stateChecked)
        val colors = intArrayOf(colorUnchecked, colorChecked)
        val colorStates = ColorStateList(states, colors)
        binding.bottomNavBar.itemIconTintList = colorStates
        binding.bottomNavBar.itemTextColor = colorStates
    }

    private fun enableAppCloseNoticeAfterDelay() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            appClosable = false
        }
    }

    private fun NavDirections.execute() = findNavController(binding.feedsContainer.id).navigate(this)
}
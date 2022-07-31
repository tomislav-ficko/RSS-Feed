package com.ficko.rssfeed.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.MainActivityBinding
import com.ficko.rssfeed.ui.base.BaseActivity
import com.ficko.rssfeed.ui.common.AppBar
import com.ficko.rssfeed.vm.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(),
    AppBar.AppBarListener {

    private val navigationViewModel by viewModels<NavigationViewModel>()
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val feedsNavController by lazy { findNavController(binding.feedsContainer) }
    private val favoritesNavController by lazy { findNavController(binding.favoritesContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        setUpActivity()
    }

    override fun onBackPressed() {
        if (feedsTabSelected()) {// TODO && initial destination is not shown
            feedsNavController.navigateUp()
        } else if (favoritesTabSelected()) {
            selectFeedsTab()
        } else {
            super.onBackPressed()
        }
    }

    override fun addButtonClicked() {
        showScreenForAddingNewFeed()
    }

    override fun backButtonClicked() {}

    private fun observeViewModel() {
        navigationViewModel.feedDetailsOpen.observe(this) { feedName ->
            binding.appBar.updateView(
                backButtonEnabled = true,
                title = feedName,
                addButtonEnabled = false
            )
        }
        navigationViewModel.feedsOpen.observe(this) {
            binding.appBar.updateView(
                backButtonEnabled = false,
                title = "",
                addButtonEnabled = true
            )
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
        binding.appBar.updateView(
            backButtonEnabled = true,
            addButtonEnabled = false,
            title = getString(R.string.title_add_new_feed)
        )
        // TODO open AddRssFeedFragment
    }

    private fun feedsTabSelected() = binding.activeTabIndex == 0

    private fun favoritesTabSelected() = binding.activeTabIndex == 1

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
}
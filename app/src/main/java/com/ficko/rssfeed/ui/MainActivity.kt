package com.ficko.rssfeed.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.MainActivityBinding
import com.ficko.rssfeed.ui.base.BaseActivity
import com.ficko.rssfeed.ui.common.AppBar
import com.ficko.rssfeed.ui.common.Utils
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.AppBarViewModel.FragmentType
import com.ficko.rssfeed.vm.AppBarViewModel.TabType
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity(),
    AppBar.AppBarListener {

    private val appBarViewModel by viewModels<AppBarViewModel>()
    private val feedViewModel by viewModels<RssFeedViewModel>()
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val feedsNavController by lazy { getNavController(binding.feedsContainer.id) }
    private val favoritesNavController by lazy { getNavController(binding.favoritesContainer.id) }
    private var appClosable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        setUpActivity()
    }

    override fun onBackPressed() {
        if (favoritesTabSelected() && !favoritesRootVisible()) {
            favoritesNavController.navigateUp()
            appBarViewModel.activeFragmentChanged(FragmentType.FEEDS)
        } else if (favoritesTabSelected() && favoritesRootVisible()) {
            selectFeedsTab()
        } else if (feedsTabSelected() && !feedsRootVisible()) {
            feedsNavController.navigateUp()
            appBarViewModel.activeFragmentChanged(FragmentType.FEEDS)
        } else if (feedsTabSelected() && !appClosable) {
            appClosable = true
            Utils.showToast(this, R.string.back_button_notice)
            enableAppCloseNoticeAfterDelay()
        } else {
            super.onBackPressed()
        }
    }

    override fun addButtonClicked() {
        appBarViewModel.activeFragmentChanged(FragmentType.NEW_FEED)
        RssFeedsFragmentDirections.actionFeedsDestinationToNewFeedFragment().execute()
    }

    override fun backButtonClicked() {
        onBackPressed()
    }

    override fun favoriteButtonClicked() {
        feedViewModel.toggleFeedFavoriteStatus()
    }

    override fun deleteButtonClicked() {
        feedViewModel.deleteFeed()
    }

    private fun observeViewModel() {
        appBarViewModel.feedsScreenOpen.observe(this) { displayAppBarForFeedsScreen(it) }
        appBarViewModel.feedDetailsScreenOpen.observe(this) { feedName -> displayAppBarForFeedDetailsScreen(feedName) }
        appBarViewModel.addNewFeedScreenOpen.observe(this) { displayAppBarForAddNewFeedScreen() }
    }

    private fun getNavController(fragmentContainerViewId: Int): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(fragmentContainerViewId) as NavHostFragment
        return navHostFragment.findNavController()
    }

    private fun setUpActivity() {
        binding.appBar.setListener(this)
        initializeFeedsNavGraph()
        initializeFavoritesNavGraph()
        setUpBottomNavBar()
    }

    private fun setUpBottomNavBar() {
        binding.activeTabIndex = 0
        binding.bottomNavBar.setOnNavigationItemSelectedListener { selectedTab ->
            updateActiveTab(selectedTab.itemId)
            true
        }
        setUpTabColors()
    }

    private fun feedsTabSelected() = binding.activeTabIndex == 0

    private fun favoritesTabSelected() = binding.activeTabIndex == 1
    private fun feedsRootVisible() = feedsNavController.currentDestination?.id == R.id.feeds_destination
    private fun favoritesRootVisible() = favoritesNavController.currentDestination?.id == R.id.feeds_destination
    private fun selectFeedsTab() {
        binding.bottomNavBar.selectedItemId = R.id.feeds_tab
        binding.activeTabIndex = 0
    }

    private fun updateActiveTab(activeItemId: Int) {
        if (activeItemId == R.id.feeds_tab) {
            binding.activeTabIndex = 0
            appBarViewModel.activeTabChanged(TabType.FEEDS)
        } else {
            binding.activeTabIndex = 1
            appBarViewModel.activeTabChanged(TabType.FAVORITES)
            initializeFavoritesNavGraph()
        }
    }

    private fun displayAppBarForFeedsScreen(shouldDisplayAddButton: Boolean) =
        updateAppBar(addButtonEnabled = shouldDisplayAddButton, title = "")

    private fun displayAppBarForAddNewFeedScreen() =
        updateAppBar(backButtonEnabled = true, title = getString(R.string.title_add_new_feed))

    private fun displayAppBarForFeedDetailsScreen(feedName: String) =
        updateAppBar(backButtonEnabled = true, title = feedName, favoriteButtonEnabled = true)

    private fun initializeFeedsNavGraph() =
        feedsNavController.setGraph(R.navigation.feeds_navigation, RssFeedsFragmentArgs(shouldDisplayFavorites = false).toBundle())

    private fun initializeFavoritesNavGraph() =
        favoritesNavController.setGraph(R.navigation.feeds_navigation, RssFeedsFragmentArgs(shouldDisplayFavorites = true).toBundle())

    private fun updateAppBar(
        backButtonEnabled: Boolean = false,
        title: String,
        favoriteButtonEnabled: Boolean = false,
        addButtonEnabled: Boolean = false,
        deleteButtonEnabled: Boolean = false
    ) = binding.appBar.updateView(backButtonEnabled, title, favoriteButtonEnabled, addButtonEnabled, deleteButtonEnabled)

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
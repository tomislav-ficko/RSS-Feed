package com.ficko.rssfeed.ui.base

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import com.ficko.rssfeed.FragmentTestingActivity
import com.ficko.rssfeed.ui.NewFeedFragment
import com.ficko.rssfeed.ui.RssFeedDetailsFragment
import com.ficko.rssfeed.ui.RssFeedsFragment
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.impl.annotations.MockK
import io.mockk.verify

@HiltAndroidTest
abstract class BaseFragmentTest : BaseActivityTest() {

    @MockK
    protected lateinit var navController: NavController

    protected fun loadFragment(fragment: Fragment, arguments: Bundle? = null) {
        when (fragment) {
            is RssFeedsFragment -> loadFragment<RssFeedsFragment>(arguments)
            is RssFeedDetailsFragment -> loadFragment<RssFeedDetailsFragment>(arguments)
            is NewFeedFragment -> loadFragment<NewFeedFragment>(arguments)
        }
    }

    private inline fun <reified T : Fragment> loadFragment(arguments: Bundle? = null) {
        launchFragmentInHiltContainer<T>(fragmentArgs = arguments) {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    private inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        fragmentFactory: FragmentFactory? = null,
        crossinline action: T.() -> Unit = {}
    ) {
        val intent = createTestingActivityIntent()
        ActivityScenario.launch<FragmentTestingActivity>(intent).onActivity { activity ->
            setActivityInstance(activity)
            fragmentFactory?.let { activity.supportFragmentManager.fragmentFactory = it }
            val fragment = activity.instantiateFragment<T>()
            activity.loadFragment(fragment, fragmentArgs)
            (fragment as T).action()
        }
    }

    private fun createTestingActivityIntent(): Intent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            FragmentTestingActivity::class.java
        )
    )

    private inline fun <reified T : Fragment> FragmentTestingActivity.instantiateFragment(): Fragment {
        return supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
    }

    private fun FragmentTestingActivity.loadFragment(fragment: Fragment, args: Bundle?) {
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
    }

    protected fun verifyDirection(direction: NavDirections) {
        verify(exactly = 1) { navController.navigate(direction) }
    }
}
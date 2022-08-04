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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
abstract class BaseFragmentTest {

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @MockK
    protected lateinit var navController: NavController

    @Before
    open fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        hiltRule.inject()
    }

    protected inline fun <reified T : Fragment> loadFragment(
        arguments: Bundle? = null,
        navController: NavController? = null
    ) {
        launchFragmentInHiltContainer<T>(fragmentArgs = arguments) {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    protected inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        fragmentFactory: FragmentFactory? = null,
        crossinline action: T.() -> Unit = {}
    ) {
        val intent = createTestingActivityIntent()
        ActivityScenario.launch<FragmentTestingActivity>(intent).onActivity { activity ->
            fragmentFactory?.let { activity.supportFragmentManager.fragmentFactory = it }
            val fragment = activity.instantiateFragment<T>()
            activity.loadFragment(fragment, fragmentArgs)
            (fragment as T).action()
        }
    }

    protected fun createTestingActivityIntent(): Intent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            FragmentTestingActivity::class.java
        )
    )

    protected inline fun <reified T : Fragment> FragmentTestingActivity.instantiateFragment(): Fragment {
        return supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
    }

    protected fun FragmentTestingActivity.loadFragment(fragment: Fragment, args: Bundle?) {
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
    }

    protected fun verifyDirection(direction: NavDirections) {
        verify(exactly = 1) { navController.navigate(direction) }
    }
}
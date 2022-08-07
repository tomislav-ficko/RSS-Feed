package com.ficko.rssfeed.ui.base

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.ficko.rssfeed.FragmentTestingActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
abstract class BaseFragmentTest {

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    lateinit var activityInstance: AppCompatActivity
    protected val anyUseCaseInProgress = MutableLiveData<Boolean>()
    protected val anyUseCaseFailed = MutableLiveData<Throwable>()

    @MockK
    protected lateinit var navController: NavController

    @Before
    open fun setUp() {
        Intents.init()
        MockKAnnotations.init(this, relaxed = true)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    protected inline fun <reified T : Fragment> loadFragment(arguments: Bundle? = null) {
        launchFragmentInHiltContainer<T>(fragmentArgs = arguments) {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        fragmentFactory: FragmentFactory? = null,
        crossinline action: T.() -> Unit = {}
    ) {
        val intent = createTestingActivityIntent()
        ActivityScenario.launch<FragmentTestingActivity>(intent).onActivity { activity ->
            activityInstance = activity as AppCompatActivity
            fragmentFactory?.let { activity.supportFragmentManager.fragmentFactory = it }
            val fragment = activity.instantiateFragment<T>()
            activity.loadFragment(fragment, fragmentArgs)
            (fragment as T).action()
        }
    }

    fun createTestingActivityIntent(): Intent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            FragmentTestingActivity::class.java
        )
    )

    inline fun <reified T : Fragment> FragmentTestingActivity.instantiateFragment(): Fragment {
        return supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
    }

    fun FragmentTestingActivity.loadFragment(fragment: Fragment, args: Bundle?) {
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
    }

    protected fun waitForUiThread(additionalSleepTimeMillis: Long? = null) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        additionalSleepTimeMillis?.let { Thread.sleep(it) }
    }

    protected fun verifyDirection(direction: NavDirections) {
        verify(exactly = 1) { navController.navigate(direction) }
    }
}
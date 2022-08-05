package com.ficko.rssfeed.ui.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.ficko.rssfeed.common.BaseMatchers
import com.ficko.rssfeed.data.local.preferences.PreferenceHandler
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
abstract class BaseActivityTest : BaseMatchers {

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var activityInstance: Activity
    protected val anyUseCaseInProgress = MutableLiveData<Boolean>()
    protected val anyUseCaseFailed = MutableLiveData<Throwable>()
    private val coroutineScope = MainScope()

    @Before
    fun setUpTest() {
        Intents.init()
        MockKAnnotations.init(this, relaxed = true)
    }

    @After
    fun tearDownTest() {
        try {
            Intents.release()
            PreferenceHandler.clearAll()
            coroutineScope.cancel()
        } catch (e: Exception) {
        }
    }

    protected inline fun <reified T : Activity> launchActivity(intent: Intent = Intent()) {
        val internalIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext, T::class.java
        ).apply {
            intent.extras?.let { putExtras(it) }
            intent.data?.let { data = it }
            flags = intent.flags
        }
        ActivityScenario.launch<T>(internalIntent).onActivity { activityInstance = it }
        mockIntentCalls()
    }

    protected fun getActivityInstance(): AppCompatActivity {
        return activityInstance as AppCompatActivity
    }

    protected fun runOnUiThread(action: () -> Unit) {
        getActivityInstance().runOnUiThread(action)
        waitForUiThread()
    }

    protected fun waitForUiThread(additionalSleepTimeMillis: Long? = null) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        additionalSleepTimeMillis?.let { Thread.sleep(it) }
    }

    protected fun mockIntentCalls() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
    }
}
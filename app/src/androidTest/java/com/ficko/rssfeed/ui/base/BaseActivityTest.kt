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

    protected abstract val activityClass: Class<out Activity>

    protected lateinit var activityScenario: ActivityScenario<out Activity>

    private lateinit var activityInstance: Activity

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

    protected fun launchActivity(intent: Intent = Intent()) {
        activityScenario = ActivityScenario.launch(
            Intent(InstrumentationRegistry.getInstrumentation().targetContext, activityClass).apply {
                intent.extras?.let { putExtras(it) }
                intent.data?.let { data = it }
                flags = intent.flags
            })
        activityScenario.onActivity { activityInstance = it }
        mockIntentCalls()
    }

    protected fun getActivityInstance(): AppCompatActivity {
        return activityInstance as AppCompatActivity
    }

    protected fun runOnUiThread(action: () -> Unit) {
        getActivityInstance().runOnUiThread(action)
        waitForUiThread()
    }

    protected fun waitForUiThread(additionalSleepTime: Long = 0) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        if (additionalSleepTime > 0)
            Thread.sleep(additionalSleepTime)
    }

    private fun mockIntentCalls() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
    }
}
package com.ficko.rssfeed.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ficko.rssfeed.data.local.preferences.PreferenceHandler
import io.mockk.MockKAnnotations
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
abstract class BaseViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {
        MockKAnnotations.init(this, relaxed = true)
        PreferenceHandler.init(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        PreferenceHandler.clearAll()
    }
}
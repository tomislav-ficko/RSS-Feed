package com.ficko.rssfeed.data.local.preferences

import android.os.Build
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M])
class PreferenceHandlerTest {

    @Before
    fun setUp() {
        val context = RuntimeEnvironment.getApplication()
        PreferenceHandler.init(context)
    }

    @Test
    fun shouldPutValue() {
        // Given
        val value = "value"

        // When
        PreferenceHandler.putValue(value)

        // Then
        PreferenceHandler.getValue() shouldBe value
    }

    @Test
    fun shouldClearAll() {
        // Given
        PreferenceHandler.putValue("value")

        // When
        PreferenceHandler.clearAll()

        // Then
        PreferenceHandler.getValue() shouldBe null
    }
}
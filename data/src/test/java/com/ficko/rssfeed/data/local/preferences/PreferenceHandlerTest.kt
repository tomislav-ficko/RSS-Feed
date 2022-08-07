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
    fun shouldPutFavoriteFeedUrls() {
        // Given
        val url = "url"

        // When
        PreferenceHandler.putFavoriteFeedUrls(setOf(url))

        // Then
        PreferenceHandler.getFavoriteFeedUrls().first() shouldBe url
    }

    @Test
    fun shouldClearAll() {
        // Given
        PreferenceHandler.putFavoriteFeedUrls(setOf("url"))

        // When
        PreferenceHandler.clearAll()

        // Then
        PreferenceHandler.getFavoriteFeedUrls().size shouldBe 0
    }
}
package com.ficko.rssfeed

import android.app.Application
import com.ficko.rssfeed.data.local.preferences.PreferenceHandler
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RssFeedApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHandler.init(this)
        Timber.plant(Timber.DebugTree())
    }
}
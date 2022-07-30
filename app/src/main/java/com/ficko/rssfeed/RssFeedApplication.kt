package com.ficko.rssfeed

import android.app.Application
import com.ficko.rssfeed.data.local.preferences.PreferenceHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RssFeedApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHandler.init(this)
    }
}
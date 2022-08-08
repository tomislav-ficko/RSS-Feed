package com.ficko.rssfeed.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHandler {

    private lateinit var sharedPreferences: SharedPreferences
    private const val FAVORITE_FEEDS_KEY = "favorite_feeds_key"

    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getFavoriteFeedUrls(): Set<String> =
        sharedPreferences.getStringSet(FAVORITE_FEEDS_KEY, null) ?: setOf()

    fun putFavoriteFeedUrls(favoriteFeedUrls: Set<String>) =
        sharedPreferences.edit().putStringSet(FAVORITE_FEEDS_KEY, favoriteFeedUrls).apply()

    fun clearAll() = sharedPreferences.edit().clear().apply()
}
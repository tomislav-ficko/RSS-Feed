package com.ficko.rssfeed.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferenceHandler {

    private lateinit var sharedPreferences: SharedPreferences
    private const val VALUE_KEY = "value_key"

    fun init(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getValue() = sharedPreferences.getString(VALUE_KEY, null)

    fun putValue(value: String) = sharedPreferences.edit().putString(VALUE_KEY, value).apply()

    fun clearAll() = sharedPreferences.edit().clear().apply()
}
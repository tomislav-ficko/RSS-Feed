package com.ficko.rssfeed.data.local.database

import androidx.room.TypeConverter
import com.ficko.rssfeed.domain.RssFeedItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromRssFeedItemList(data: List<RssFeedItem?>?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toRssFeedItemList(data: String?): List<RssFeedItem?>? {
        val listType = object : TypeToken<List<*>?>() {}.type
        return gson.fromJson(data, listType)
    }
}
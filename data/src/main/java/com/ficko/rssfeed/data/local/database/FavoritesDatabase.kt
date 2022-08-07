package com.ficko.rssfeed.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ficko.rssfeed.data.local.database.dao.RssFeedDao
import com.ficko.rssfeed.data.local.database.dto.RssFeedDto

@Database(
    exportSchema = false,
    entities = [RssFeedDto::class],
    version = 4
)
@TypeConverters(DatabaseConverters::class)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun rssFeedDao(): RssFeedDao
}
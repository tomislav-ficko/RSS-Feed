package com.ficko.rssfeed.data.local.database.dao

import androidx.room.*
import com.ficko.rssfeed.data.local.database.dto.RssFeedDto

@Dao
interface RssFeedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg feeds: RssFeedDto)

    @Query("SELECT * FROM feeds WHERE `id` IN (:feedId)")
    suspend fun get(feedId: String): RssFeedDto

    @Query("SELECT * FROM feeds")
    suspend fun getAll(): List<RssFeedDto>

    @Query("DELETE FROM feeds WHERE `id` IN (:feedId)")
    suspend fun delete(feedId: String)

    @Transaction
    suspend fun delete(feed: RssFeedDto) = delete(feed.id)

    @Query("DELETE FROM feeds")
    suspend fun deleteAll()
}
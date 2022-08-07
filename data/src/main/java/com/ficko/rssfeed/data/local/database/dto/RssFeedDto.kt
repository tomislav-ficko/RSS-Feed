package com.ficko.rssfeed.data.local.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ficko.rssfeed.domain.RssFeedItem

@Entity(tableName = "feeds")
data class RssFeedDto(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val rssUrl: String,
    var url: String?,
    var name: String?,
    var description: String?,
    var imageUrl: String?,
    var items: List<RssFeedItem>?
)
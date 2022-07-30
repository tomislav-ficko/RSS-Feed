package com.ficko.rssfeed.data.local.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ficko.rssfeed.domain.RssFeedItem

@Entity(tableName = "feeds")
class RssFeedDto {

    @PrimaryKey
    var id = ""

    var url = ""
    var name = ""
    var description = ""
    var imageUrl = ""
    var items: List<RssFeedItem> = listOf()
}
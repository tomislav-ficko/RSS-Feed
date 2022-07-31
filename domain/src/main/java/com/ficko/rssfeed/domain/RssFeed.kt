package com.ficko.rssfeed.domain

import java.io.Serializable

class RssFeed : Serializable {
    var id = ""
    var url = ""
    var name = ""
    var description = ""
    var imageUrl = ""
    var items: List<RssFeedItem> = listOf()
}
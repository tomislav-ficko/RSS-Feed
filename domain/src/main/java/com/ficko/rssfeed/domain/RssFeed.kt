package com.ficko.rssfeed.domain

class RssFeed {
    var url = ""
    var name = ""
    var description = ""
    var imageUrl = ""
    var items: List<RssFeedItem> = listOf()
}
package com.ficko.rssfeed.domain

class RssFeed : CommonRssAttributes() {
    var rssUrl: String = ""
    var items: List<RssFeedItem> = listOf()
}
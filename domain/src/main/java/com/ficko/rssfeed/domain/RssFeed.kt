package com.ficko.rssfeed.domain

import java.io.Serializable

class RssFeed : CommonRssAttributes(), Serializable {
    var items: List<RssFeedItem> = listOf()
}
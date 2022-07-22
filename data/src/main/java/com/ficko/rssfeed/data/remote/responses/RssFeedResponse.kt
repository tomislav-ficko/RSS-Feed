package com.ficko.rssfeed.data.remote.responses

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RssFeedResponse(

    @Element(name = "title")
    @Path("channel")
    var title: String? = null,

    @Element(name = "description")
    @Path("channel")
    var description: String? = null,

    @Element(name = "link")
    @Path("channel")
    var link: String? = null,

    @Element(name = "url")
    @Path("channel/image")
    var imageUrl: String? = null,

    @ElementList(name = "item", inline = true, required = false)
    @Path("channel")
    var items: List<RssFeedItemResponse>? = null
)
package com.ficko.rssfeed.data.remote.responses

import org.simpleframework.xml.*

@Root(name = "rss", strict = false)
data class RssFeedResponse(

    @field:Element
    @field:Path("channel")
    var title: String? = null,

    @field:Element
    @field:Path("channel")
    var description: String? = null,

    @field:Element(required = false)
    @field:Path("channel")
    var link: String? = null,

    @field:Element(name = "url", required = false)
    @field:Path("channel/image")
    var imageUrl: String? = null,

    @field:ElementList(inline = true, required = false)
    @field:Path("channel")
    var items: List<RssFeedItemResponse>? = null
)

@Root(name = "item", strict = false)
data class RssFeedItemResponse constructor(

    @field:Element
    var title: String? = null,

    @field:Element(required = false)
    var description: String? = null,

    @field:Element(required = false)
    var link: String? = null,

    @field:Attribute(name = "url", required = false)
    @field:Path("media:group/media:content[0]")
    var imageUrl: String? = null
)
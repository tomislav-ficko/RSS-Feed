package com.ficko.rssfeed.data.remote.responses

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class RssFeedItemResponse @JvmOverloads constructor(

    @Element(name = "title")
    var title: String? = null,

    @Element(name = "link")
    var link: String? = null,

    @Element(name = "description")
    var description: String? = null,

    @Attribute(name = "url", required = false)
    @Path("media:group/media:content[0]")
    var imageUrl: String? = null
)
package com.ficko.rssfeed.domain

import java.io.Serializable

abstract class CommonRssAttributes : Serializable {

    var id = ""
    var url = ""
    var name = ""
    var description = ""
    var imageUrl = ""
}
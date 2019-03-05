package com.kylestrait.donttalktunatome.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "enclosure", strict = false)
class Enclosure {

    @field:Attribute(name = "length", required = false)
    var length: String? = null

    @field:Attribute(name = "url", required = false)
    var url: String? = null
}
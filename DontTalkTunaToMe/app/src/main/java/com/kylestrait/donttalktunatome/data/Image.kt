package com.kylestrait.donttalktunatome.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "image", strict = false)
@Namespace(prefix = "itunes")
class Image {

    @field:Attribute(name = "href", required = false)
    var href: String? = null
}
package com.kylestrait.donttalktunatome.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "image", strict = false)
@Namespace(prefix = "itunes")
class Image {

    @field:Attribute(name = "href", required = false)
    var href: String? = null

    constructor()

    constructor(href: String?) {
        this.href = href
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (href != other.href) return false

        return true
    }

    override fun hashCode(): Int {
        return href?.hashCode() ?: 0
    }


}
package com.kylestrait.donttalktunatome.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "channel", strict = false)
class Channel {

    @field:ElementList(name = "item", inline = true)
    var item: List<Item>? = null

    @field:Element(name = "title", required = false)
    var title: String? = null
}
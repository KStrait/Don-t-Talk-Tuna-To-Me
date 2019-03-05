package com.kylestrait.donttalktunatome.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "rss", strict = false)
class RSS {

    @field:Element(name = "channel", required = false)
    var channel: Channel? = null
}
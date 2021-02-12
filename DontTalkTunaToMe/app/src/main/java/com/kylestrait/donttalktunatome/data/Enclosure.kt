package com.kylestrait.donttalktunatome.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Entity
@Root(name = "enclosure", strict = false)
class Enclosure {

    constructor()

    constructor(url: String){
        this.url = url
        this.length = ""
    }

    @field:Attribute(name = "length", required = false)
    var length: String? = null

    @NonNull
    @PrimaryKey
    @field:Attribute(name = "url", required = false)
    var url: String? = null
}
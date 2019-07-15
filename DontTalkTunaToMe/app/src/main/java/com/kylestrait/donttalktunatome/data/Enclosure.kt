package com.kylestrait.donttalktunatome.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
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
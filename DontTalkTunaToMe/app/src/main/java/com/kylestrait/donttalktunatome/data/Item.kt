package com.kylestrait.donttalktunatome.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import org.simpleframework.xml.*
import kotlin.jvm.Transient

@Entity
@Root(name = "item", strict = false)
class Item {

//    @field:Element(name = "title", required = false)
//    var title: String? = null

    @NonNull
    @PrimaryKey
    @field:Path("title")
    @field:Text(required = false)
    var title: String? = null

    @field:Element(name = "pubDate", required = false)
    var pubDate: String? = null

    @field:Element(name = "description", required = false)
    var description: String? = null

    @field:Element(name = "enclosure", required = false)
    var enclosure: Enclosure? = null

    @field:Element(name = "image", required = false)
    @field:Namespace(prefix = "itunes")
    var image: Image? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (title != other.title) return false
        if (pubDate != other.pubDate) return false
        if (description != other.description) return false
        if (enclosure != other.enclosure) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (pubDate?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (enclosure?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        return result
    }
}
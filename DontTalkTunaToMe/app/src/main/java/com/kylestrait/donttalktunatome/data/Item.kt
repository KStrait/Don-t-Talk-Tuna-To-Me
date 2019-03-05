package com.kylestrait.donttalktunatome.data

import android.os.Parcel
import android.os.Parcelable
import org.simpleframework.xml.*
import retrofit2.http.Field


@Root(name = "item", strict = false)
class Item {

//    @field:Element(name = "title", required = false)
//    var title: String? = null

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
}
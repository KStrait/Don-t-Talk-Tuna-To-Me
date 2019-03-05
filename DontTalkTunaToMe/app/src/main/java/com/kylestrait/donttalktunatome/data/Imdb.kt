package com.kylestrait.donttalktunatome.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.squareup.moshi.Json

class Imdb : Parcelable {

    @Json(name = "page")
    var page: Int? = null
    @Json(name = "total_results")
    var total_results: Int? = null
    @Json(name = "total_pages")
    var total_pages: Int? = null
    @Json(name = "results")
    var results: List<Result>? = null

    protected constructor(`in`: Parcel) {
        this.page = `in`.readValue(Int::class.java.classLoader) as Int
        this.total_results = `in`.readValue(Int::class.java.classLoader) as Int
        this.total_pages = `in`.readValue(Int::class.java.classLoader) as Int
        `in`.readList(this.results, com.kylestrait.donttalktunatome.data.Result::class.java.classLoader)
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(page)
        dest.writeValue(total_results)
        dest.writeValue(total_pages)
        dest.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Imdb> = object : Creator<Imdb> {

            override fun createFromParcel(`in`: Parcel): Imdb {
                return Imdb(`in`)
            }

            override fun newArray(size: Int): Array<Imdb?> {
                return arrayOfNulls(size)
            }

        }
    }

}
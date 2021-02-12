package com.kylestrait.donttalktunatome.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.squareup.moshi.Json

class Result : Parcelable {

    @Json(name = "vote_count")
    var vote_count: Int? = null
    @Json(name = "id")
    var id: Int? = null
    @Json(name = "video")
    var video: Boolean? = null
    @Json(name = "vote_average")
    var vote_average: Double? = null
    @Json(name = "title")
    var title: String? = null
    @Json(name = "popularity")
    var popularity: Double? = null
    @Json(name = "poster_path")
    var poster_path: String? = null
    @Json(name = "original_language")
    var original_language: String? = null
    @Json(name = "original_title")
    var original_title: String? = null
    @Json(name = "genre_ids")
    var genre_ids: List<Int>? = null
    @Json(name = "backdrop_path")
    var backdrop_path: String? = null
    @Json(name = "adult")
    var adult: Boolean? = null
    @Json(name = "overview")
    var overview: String? = null
    @Json(name = "release_date")
    var release_date: String? = null

    protected constructor(`in`: Parcel) {
        this.vote_count = `in`.readValue(Int::class.java.classLoader) as Int
        this.id = `in`.readValue(Int::class.java.classLoader) as Int
        this.video = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.vote_average = `in`.readValue(Double::class.java.classLoader) as Double
        this.title = `in`.readValue(String::class.java.classLoader) as String
        this.popularity = `in`.readValue(Double::class.java.classLoader) as Double
        this.poster_path = `in`.readValue(String::class.java.classLoader) as String
        this.original_language = `in`.readValue(String::class.java.classLoader) as String
        this.original_title = `in`.readValue(String::class.java.classLoader) as String
        this.genre_ids?.let { `in`.readList(it, Int::class.java.classLoader) }
        this.backdrop_path = `in`.readValue(String::class.java.classLoader) as String
        this.adult = `in`.readValue(Boolean::class.java.classLoader) as Boolean
        this.overview = `in`.readValue(String::class.java.classLoader) as String
        this.release_date = `in`.readValue(String::class.java.classLoader) as String
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(vote_count)
        dest.writeValue(id)
        dest.writeValue(video)
        dest.writeValue(vote_average)
        dest.writeValue(title)
        dest.writeValue(popularity)
        dest.writeValue(poster_path)
        dest.writeValue(original_language)
        dest.writeValue(original_title)
        dest.writeList(genre_ids)
        dest.writeValue(backdrop_path)
        dest.writeValue(adult)
        dest.writeValue(overview)
        dest.writeValue(release_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Result> = object : Creator<Result> {

            override fun createFromParcel(`in`: Parcel): Result {
                return Result(`in`)
            }

            override fun newArray(size: Int): Array<Result?> {
                return arrayOfNulls(size)
            }

        }
    }

}
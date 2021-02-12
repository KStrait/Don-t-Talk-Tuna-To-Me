package com.kylestrait.donttalktunatome.network

import com.kylestrait.donttalktunatome.data.RSS
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {

    @GET("rss")
    fun getMainFeed() : Deferred<Response<RSS>>
}
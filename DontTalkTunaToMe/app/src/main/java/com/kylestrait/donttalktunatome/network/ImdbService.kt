package com.kylestrait.donttalktunatome.network

import com.kylestrait.donttalktunatome.data.Imdb
import dagger.Provides
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


interface ImdbService {

    @GET("movie")
    fun getMainFeed(@Query("api_key") key: String, @Query("query") movie: String) : Deferred<Response<Imdb>>
}
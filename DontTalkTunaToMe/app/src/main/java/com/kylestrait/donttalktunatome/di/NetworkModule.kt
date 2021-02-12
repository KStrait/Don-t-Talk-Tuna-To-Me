package com.kylestrait.donttalktunatome.di

import android.util.Log
import com.kylestrait.donttalktunatome.data.Imdb
import com.kylestrait.donttalktunatome.data.RSS
import com.kylestrait.donttalktunatome.network.DownloadService
import com.kylestrait.donttalktunatome.network.ImdbService
import com.kylestrait.donttalktunatome.network.NetworkService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor

@Module
class NetworkModule @Inject constructor() {
    private val networkApi: NetworkService
    private val imdbService: ImdbService
    private val downloadService: DownloadService

    init {
        val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging)  // <-- this is the important line!

        val retrofit = Retrofit.Builder()
//            .baseUrl("https://donttalktuna.libsyn.com/")
            .baseUrl("https://poddontlie.libsyn.com/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        networkApi = retrofit.create(NetworkService::class.java)


        val imdbRetrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/search/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        imdbService = imdbRetrofit.create(ImdbService::class.java)

        val downloadRetrofit = Retrofit.Builder().baseUrl("http://traffic.libsyn.com/donttalktuna/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        downloadService = downloadRetrofit.create(DownloadService::class.java)
    }

    @Singleton
    @Provides
    fun getImdbFeed(apiKey: String, movie: String): Deferred<Response<Imdb>> {
        return imdbService.getMainFeed(apiKey, movie)
    }

    @Singleton
    @Provides
    fun getRssFeed(): Deferred<Response<RSS>> {
        return networkApi.getMainFeed()
    }

    @Singleton
    @Provides
    fun getDownload(url: String): Deferred<Response<ResponseBody>> {
        return downloadService.downloadFile(url)
    }
}
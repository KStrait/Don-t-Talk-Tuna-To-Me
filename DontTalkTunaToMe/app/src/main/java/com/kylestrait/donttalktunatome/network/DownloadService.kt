package com.kylestrait.donttalktunatome.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadService {
    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String) : Deferred<Response<ResponseBody>>
}
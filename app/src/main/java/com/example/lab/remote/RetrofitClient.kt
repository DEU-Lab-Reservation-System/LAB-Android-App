package com.example.lab.remote

import com.example.lab.utils.ApiLogger
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://43.200.142.116"

    var retrofit: Retrofit

    init {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

        val interceptor = HttpLoggingInterceptor(ApiLogger())

        // Http 요청/응답 중 Body만 로깅
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client:OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}
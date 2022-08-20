package com.example.dashboardkiosdeliv.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val okhttp = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttp)
        .build()

    fun myApiClient() = retrofit.create(APIClient::class.java)

}
package com.example.dashboardkiosdeliv.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private fun getRetrofitClient(): Retrofit{

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.6/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInstance(): APIClient {

        return getRetrofitClient().create(APIClient::class.java)
    }
}
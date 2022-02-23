package com.example.dashboardkiosdeliv.network

import com.example.dashboardkiosdeliv.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface APIClient {
    @FormUrlEncoded
    @POST ("skripsi/login.php")

    fun login(
        @Field("post_email") email : String,
        @Field("post_password") password : String
    ): Call<ResponseLogin>

}
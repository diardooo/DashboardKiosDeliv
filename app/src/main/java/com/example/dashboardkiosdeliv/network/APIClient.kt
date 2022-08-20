package com.example.dashboardkiosdeliv.network

import com.example.dashboardkiosdeliv.model.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface APIClient {

    //LOGIN------------------------------------------------
    @FormUrlEncoded
    @POST ("skripsi2/public/api/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<ResponseLogin>

    //DEPOSIT------------------------------------------------
    @GET("skripsi2/public/api/deposit")
    fun infoDeposit(
    ): Call<ResponseDeposit>

    @FormUrlEncoded
    @POST("skripsi2/public/api/deposit/filter")
    fun infoDepositFilter(
        @Field("start_date") start_date : String,
        @Field("end_date") end_date : String
    ): Call<ResponseDepositFilter>

    //CHANGE PASSWORD -----------------------------------------
    @FormUrlEncoded
    @POST("skripsi2/public/api/changepassword")
    fun changePassword(
        @Field("email") email : String,
        @Field("password_lama") password_lama : String,
        @Field("password_baru") password_baru : String,
    ): Call<ResponseChangePass>

    //PROGRES LOKET --------------------------------------------
    @GET("skripsi2/public/api/merchant")
    fun progresLoket(
    ): Call<ResponseProgresLoket>

    @FormUrlEncoded
    @POST("skripsi2/public/api/merchant/filter")
    fun progresLoketFilter(
        @Field("start_date") start_date : String,
        @Field("end_date") end_date : String
    ): Call<ResponseProgresLoketFilter>

    //DASHBOARD
    @GET("skripsi2/public/api/dashboard")
    fun dashboard(
    ): Call<ResponseDashboard>

    @FormUrlEncoded
    @POST("skripsi2/public/api/dashboard/filter")
    fun dashboardFilter(
        @Field("start_date") start_date: String,
        @Field("end_date") end_date: String
    ): Call<ResponseDashboardFilter>

}
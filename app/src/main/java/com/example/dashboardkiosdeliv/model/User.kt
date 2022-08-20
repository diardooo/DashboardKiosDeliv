package com.example.dashboardkiosdeliv.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseLogin (
    @SerializedName("success") var success : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("user") var user : User,
    @SerializedName("token") var token : String,
        )

@Parcelize
@Entity
class User (
    @SerializedName ("id") val id  : String,
    @SerializedName ("name") val name : String,
    @SerializedName ("email") val email : String,
    @SerializedName ("level") val level : String,
    @SerializedName ("photo") val photo : String,
    @SerializedName ("email_verified_at") val email_verified_at : String,
    @SerializedName ("created_at") val created_at : String,
    @SerializedName ("updated_at") val updated_at : String,
): Parcelable

package com.example.dashboardkiosdeliv.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class ResponseChangePass (
    @SerializedName("success") var success : Boolean,
    @SerializedName("message") var message : String,
        )
package com.example.dashboardkiosdeliv.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseDepositFilter (
    @SerializedName("success") var success : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data_deposit_filter : data_deposit_filter
        )

@Parcelize
@Entity
class data_deposit_filter (
    @SerializedName ("deposit_terbanyak") var deposit_terbanyak : deposit_terbanyak_filter,
    @SerializedName ("deposit") var deposit : ArrayList<deposit_filter>
        ):Parcelable

@Parcelize
@Entity
class deposit_terbanyak_filter (
    @SerializedName ("bank_id") var bankid  : Int,
    @SerializedName ("total") var total : String,
    @SerializedName ("name") var name : String,
    @SerializedName ("bulan_lalu") var bulan_lalu : String,
    @SerializedName ("total_deposit") var total_deposit : String,
        ):Parcelable

@Parcelize
@Entity
class deposit_filter (
    @SerializedName ("bank_id") var bank_id : Int,
    @SerializedName ("total") var total : String,
    @SerializedName ("name") var name : String
        ):Parcelable
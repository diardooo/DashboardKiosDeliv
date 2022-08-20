package com.example.dashboardkiosdeliv.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseDeposit (
    @SerializedName("success") var success : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data_deposit : data_deposit,
    @SerializedName("tanggal") var tanggal_info_deposit : String
        )

@Parcelize
@Entity
class data_deposit (
    @SerializedName ("deposit_terbanyak") var deposit_terbanyak : deposit_terbanyak,
    @SerializedName ("deposit") var deposit : ArrayList<deposit>
        ):Parcelable

@Parcelize
@Entity
class deposit_terbanyak (
    @SerializedName ("bank_id") var bankid  : Int,
    @SerializedName ("total") var total : String,
    @SerializedName ("name") var name : String,
    @SerializedName ("bulan_lalu") var bulan_lalu : String,
    @SerializedName ("total_deposit") var total_deposit : String,
    ):Parcelable

@Parcelize
@Entity
class deposit (
    @SerializedName ("bank_id") var bank_id : Int,
    @SerializedName ("total") var total : String,
    @SerializedName ("name") var name : String
        ):Parcelable
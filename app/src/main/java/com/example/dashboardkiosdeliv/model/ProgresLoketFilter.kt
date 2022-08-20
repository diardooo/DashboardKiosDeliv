package com.example.dashboardkiosdeliv.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseProgresLoketFilter (
    @SerializedName("success") var success : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data_merchant_filter : data_merchant_filter
        )

@Parcelize
@Entity
class data_merchant_filter (
    @SerializedName ("total_all_merchant") var total_all_merchant : Int,
    @SerializedName ("merchant_1_tahun") var merchant_1_tahun : ArrayList<merchant_bar_chart_filter>,
    @SerializedName ("tren_pertumbuhan_loket") var tren_loket : tren_loket_filter,
    @SerializedName ("akuisisi_merchant") var akuisisi_merchant : akuisisi_merchant_filter,
        ):Parcelable

@Parcelize
@Entity
class merchant_bar_chart_filter(
    @SerializedName ("data") var data_bar_chart : Int,
    @SerializedName ("year") var year : Int,
    @SerializedName ("month") var month : Int,
        ):Parcelable

@Parcelize
@Entity
class tren_loket_filter(
    @SerializedName ("tren_pertumbuhan_loket") var jumlah_tren_loket : Int,
    @SerializedName ("tren_pertumbuhan_loket_bulan_lalu") var jumlah_tren_loket_bulan_lalu : Int,
    @SerializedName ("indikator_pertumbuhan_merchant") var indikator_tren_loket : Float,
        ):Parcelable

@Parcelize
@Entity
class akuisisi_merchant_filter(
    @SerializedName ("akuisisi_loket_bulan_ini") var akuisisi_loket_bulan_ini : ArrayList<data_akuisisi_loket_bulan_ini_filter>,
    @SerializedName ("akuisisi_loket_bulan_lalu") var akuisisi_loket_bulan_lalu : ArrayList<data_akuisisi_loket_bulan_lalu_filter>,
        ):Parcelable

@Parcelize
@Entity
class data_akuisisi_loket_bulan_ini_filter(
    @SerializedName ("registered_by") var registered_by : String,
    @SerializedName ("total") var total_registered_by_bulan_ini : Int,
        ):Parcelable

@Parcelize
@Entity
class data_akuisisi_loket_bulan_lalu_filter(
    @SerializedName ("registered_by") var registered_by : String,
    @SerializedName ("total") var total_registered_by_bulan_lalu : Int,
        ):Parcelable
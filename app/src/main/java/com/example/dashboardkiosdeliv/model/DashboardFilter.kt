package com.example.dashboardkiosdeliv.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.nio.channels.FileLock

data class ResponseDashboardFilter (
    @SerializedName("success") var success : Boolean,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data_dashboard_filter : data_dashboard_filter
)

@Parcelize
@Entity
class data_dashboard_filter (
    @SerializedName ("total_transaksi") var total_transaksi : Int,
    @SerializedName ("total_transaksi_bulan_lalu") var total_transaksi_bulan_lalu : Int,
    @SerializedName ("indikator") var indikator : String,
    @SerializedName ("total_pendapatan") var total_pendapatan : data_total_pendapatan_filter,
    @SerializedName ("total_uang_service") var total_uang_service : ArrayList<data_total_uang_service_filter>,
    @SerializedName ("total_uang_vendor") var total_uang_vendor : ArrayList<data_total_uang_vendor_filter>,
    @SerializedName ("total_transaksi_service_complete") var transaksi_service_complete : data_total_transaksi_service_complete_filter,
    @SerializedName ("total_transaksi_service_failed") var transaksi_service_failed : data_total_transaksi_service_failed_filter,
    @SerializedName ("transaksi_vendor") var transaksi_vendor : ArrayList<data_transaksi_vendor_filter>,
    @SerializedName ("transaksi_vendor_bulan_lalu") var transaksi_vendor_bulan_lalu : ArrayList<data_transaksi_vendor_bulan_lalu_filter>,
):Parcelable

@Parcelize
@Entity
class data_total_pendapatan_filter (
    @SerializedName ("total_pendapatan_kotor") var total_pendapatan_kotor : Float,
    @SerializedName ("total_pendapatan_kotor_bulan_lalu") var total_pendapatan_kotor_bulan_lalu : Float,
    @SerializedName ("indikator_total_pendapatan_kotor") var indikator_total_pendapatan_kotor : String,
    @SerializedName ("total_komisi") var total_komisi : Float,
    @SerializedName ("total_komisi_bulan_lalu") var total_komisi_bulan_lalu : Float,
    @SerializedName ("indikator_total_komisi") var indikator_total_komisi : String,
    @SerializedName ("total_pendapatan_bersih") var total_pendapatan_bersih : Float,
    @SerializedName ("total_pendapatan_bersih_bulan_lalu") var total_pendapatan_bersih_bulan_lalu : Float,
    @SerializedName ("indikator_total_pendapatan_bersih") var indikator_total_pendapatan_bersih : String,
):Parcelable

@Parcelize
@Entity
class data_total_uang_service_filter(
    @SerializedName("service_id") var service_id : Int,
    @SerializedName("total_pendapatan") var total_pendapatan : Int,
    @SerializedName("name") var name: String,
):Parcelable

@Parcelize
@Entity
class data_total_uang_vendor_filter(
    @SerializedName("vendor_id") var vendor_id : Int,
    @SerializedName("total_pendapatan") var total_pendapatan : Int,
    @SerializedName("name") var name: String,
):Parcelable


@Parcelize
@Entity
class data_total_transaksi_service_complete_filter (
    @SerializedName ("total_transaksi_service_sukses") var total_transaksi_service_sukses : ArrayList<data_total_transaksi_service_sukses_filter>,
    @SerializedName ("indikator_total_transaksi_service_sukses") var indikator_total_transaksi_service_sukses : String,
    @SerializedName ("total_transaksi_service_sukses_terbanyak") var total_transaksi_service_sukses_terbanyak : data_total_transaksi_service_sukses_terbanyak_filter,
):Parcelable

@Parcelize
@Entity
class data_total_transaksi_service_sukses_filter(
    @SerializedName ("service_id") var service_id : Int,
    @SerializedName ("total") var total : Int,
    @SerializedName ("name") var name : String,
):Parcelable

@Parcelize
@Entity
class data_total_transaksi_service_sukses_terbanyak_filter(
    @SerializedName ("service_id") var id : Int,
    @SerializedName ("total") var total : Int,
    @SerializedName ("name") var name : String,
    @SerializedName ("bulan_lalu") var bulan_lalu : Int,
):Parcelable

@Parcelize
@Entity
class data_total_transaksi_service_failed_filter(
    @SerializedName ("total_transaksi_service_failed") var total_transaksi_service_gagal : ArrayList<data_total_transaksi_service_gagal_filter>,
    @SerializedName ("indikator_total_transaksi_service_failed") var indikator_total_transaksi_service_failed : String,
    @SerializedName ("total_transaksi_service_failed_terbanyak") var total_transaksi_service_gagal_terbanyak : data_total_transaksi_service_gagal_terbanyak_filter,
):Parcelable

@Parcelize
@Entity
class data_total_transaksi_service_gagal_filter(
    @SerializedName ("service_id") var service_id : Int,
    @SerializedName ("total") var total : Int,
    @SerializedName ("name") var name : String,
):Parcelable

@Parcelize
@Entity
class data_total_transaksi_service_gagal_terbanyak_filter(
    @SerializedName ("service_id") var id : Int,
    @SerializedName ("total") var total : Int,
    @SerializedName ("name") var name : String,
    @SerializedName ("bulan_lalu") var bulan_lalu : Int,
):Parcelable

@Parcelize
@Entity
class data_transaksi_vendor_filter (
    @SerializedName ("vendor_id") var vendor_id : Int,
    @SerializedName ("total_transaksi") var total_transaksi : Int,
    @SerializedName ("balance") var balance : Int,
    @SerializedName ("name") var name : String,
):Parcelable

@Parcelize
@Entity
class data_transaksi_vendor_bulan_lalu_filter (
    @SerializedName ("vendor_id") var vendor_id : Int,
    @SerializedName ("total_transaksi") var total_transaksi : Int,
    @SerializedName ("name") var name : String,
):Parcelable

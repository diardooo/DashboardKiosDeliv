package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.dashboardkiosdeliv.model.ResponseProgresLoket
import com.example.dashboardkiosdeliv.model.ResponseProgresLoketFilter
import com.example.dashboardkiosdeliv.network.RetrofitClient
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_informasi_deposit.*
import kotlinx.android.synthetic.main.activity_progress_loket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgressLoketActivity : AppCompatActivity() {

    private lateinit var tvDatePickerFrom : TextView
    private lateinit var btnDatePickerFrom : CardView
    private lateinit var tvDatePickerTo : TextView
    private lateinit var btnDatePickerTo : CardView
    private lateinit var barChart : BarChart
    private lateinit var startDate : String
    private lateinit var endDate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_loket)

        //INISIALISASI
        var btnBack = findViewById<ImageView>(R.id.img_back_progress_loket)

        tvDatePickerFrom = findViewById(R.id.tv_date_from_loket)
        btnDatePickerFrom = findViewById(R.id.btn_date_form_loket)
        tvDatePickerTo = findViewById(R.id.tv_date_to_loket)
        btnDatePickerTo = findViewById(R.id.btn_date_to_loket)
        barChart = findViewById(R.id.chart_progres_loket)

        //DATE PICKER
        val datePickerFrom = Calendar.getInstance()
        val datePickerTo = Calendar.getInstance()

        val datePicker1 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            datePickerFrom.set(Calendar.YEAR, year)
            datePickerFrom.set(Calendar.MONTH, month)
            datePickerFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateFrom(datePickerFrom)
        }

        val datePicker2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            datePickerTo.set(Calendar.YEAR, year)
            datePickerTo.set(Calendar.MONTH, month)
            datePickerTo.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateTo(datePickerTo)
        }

        //EVENT
        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        btnDatePickerFrom.setOnClickListener {
            DatePickerDialog(this, datePicker1,
                datePickerFrom.get(Calendar.YEAR),
                datePickerFrom.get(Calendar.MONTH),
                datePickerFrom.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnDatePickerTo.setOnClickListener {
            DatePickerDialog(this, datePicker2,
                datePickerTo.get(Calendar.YEAR),
                datePickerTo.get(Calendar.MONTH),
                datePickerTo.get(Calendar.DAY_OF_MONTH)).show()
        }

        //Set Current Date on View
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)

//        tv_date_from_loket?.text = (sdf.format(Date()))
//        tv_date_to_loket?.text = (sdf.format(Date()))

        loadProgresLoket()
        loadProgresLoketFilter()
    }

    private fun loadProgresLoket(){

        val loading = LoadingDialog(this)
        loading.startLoading()

        RetrofitClient.myApiClient().progresLoket().enqueue(object : Callback<ResponseProgresLoket>{
            override fun onResponse(
                call: Call<ResponseProgresLoket>,
                response: Response<ResponseProgresLoket>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {

                        loading.dismissLoading()

                        val currencyformat = DecimalFormat("#,###")

                        tv_date_from_loket?.text =  "01 " + it.tanggal_progres_loket
                        tv_date_to_loket?.text = "30 " + it.tanggal_progres_loket

                        Toast.makeText(this@ProgressLoketActivity,"Menampilkan otomatis bulan " + it.tanggal_progres_loket,Toast.LENGTH_LONG).show()

                        //SET BAR CHART DATA----------------------------------------------
                        var year = it.data_merchant.merchant_1_tahun[1].year.toString()
                        var bulan1 = it.data_merchant.merchant_1_tahun[6].month.toString()
                        var bulan2 = it.data_merchant.merchant_1_tahun[5].month.toString()
                        var bulan3 = it.data_merchant.merchant_1_tahun[4].month.toString()
                        var bulan4 = it.data_merchant.merchant_1_tahun[3].month.toString()
                        var bulan5 = it.data_merchant.merchant_1_tahun[2].month.toString()
                        var bulan6 = it.data_merchant.merchant_1_tahun[1].month.toString()
                        var bulan7 = it.data_merchant.merchant_1_tahun[0].month.toString()

                        // X Axis values
                        val xValues = ArrayList<String>()
                        xValues.add(bulan1 + ", " + year)
                        xValues.add(bulan2 + ", " + year)
                        xValues.add(bulan3 + ", " + year)
                        xValues.add(bulan4 + ", " + year)
                        xValues.add(bulan5 + ", " + year)
                        xValues.add(bulan6 + ", " + year)
                        xValues.add(bulan7 + ", " + year)

                        var nilai1 = it.data_merchant.merchant_1_tahun[6].data_bar_chart.toString()
                        var nilai2 = it.data_merchant.merchant_1_tahun[5].data_bar_chart.toString()
                        var nilai3 = it.data_merchant.merchant_1_tahun[4].data_bar_chart.toString()
                        var nilai4 = it.data_merchant.merchant_1_tahun[3].data_bar_chart.toString()
                        var nilai5 = it.data_merchant.merchant_1_tahun[2].data_bar_chart.toString()
                        var nilai6 = it.data_merchant.merchant_1_tahun[1].data_bar_chart.toString()
                        var nilai7 = it.data_merchant.merchant_1_tahun[0].data_bar_chart.toString()

                        // y axis values or bar data
                        val barEntry = ArrayList<BarEntry>()
                        barEntry.add(BarEntry(nilai1.toFloat(), 0))
                        barEntry.add(BarEntry(nilai2.toFloat(), 1))
                        barEntry.add(BarEntry(nilai3.toFloat(), 2))
                        barEntry.add(BarEntry(nilai4.toFloat(), 3))
                        barEntry.add(BarEntry(nilai5.toFloat(), 4))
                        barEntry.add(BarEntry(nilai6.toFloat(), 5))
                        barEntry.add(BarEntry(nilai7.toFloat(), 6))
//                        barEntry.add(BarEntry(nilai8.toFloat(), 7))

                        // Bar data set
                        val barDataSet = BarDataSet(barEntry, "Bar Chart")

                        val colors = arrayListOf<Int>()
                        colors.add(ColorTemplate.rgb("#61d4b3"))
                        colors.add(ColorTemplate.rgb("#fdd365"))
                        colors.add(ColorTemplate.rgb("#f0134d"))
                        colors.add(ColorTemplate.rgb("#fb8d62"))
                        colors.add(ColorTemplate.rgb("#fd2eb3"))
                        colors.add(ColorTemplate.rgb("#ff677d"))
                        colors.add(ColorTemplate.rgb("#ff9d9d"))
                        colors.add(ColorTemplate.rgb("#633a82"))
                        colors.random()
                        barDataSet.colors = colors
                        barDataSet.valueTextSize = 10f
                        barDataSet.valueTextColor = R.color.black

                        val dataBar = BarData(xValues, barDataSet)
                        barChart.data = dataBar
                        barChart.setBackgroundColor(resources.getColor(R.color.white))
                        barChart.animateXY(1100,1100)
                        barChart.setDescription("")


                        //Set Tren Pertumbuhan Loket
                        var indikator_tren = it.data_merchant.tren_loket.indikator_tren_loket.toString()
                        var tren_pertumbuhan_loket = it.data_merchant.tren_loket.jumlah_tren_loket.toString()
                        var tren_pertumbuhan_loket_bulan_lalu = it.data_merchant.tren_loket.jumlah_tren_loket_bulan_lalu.toString()

                        if (tren_pertumbuhan_loket.toInt() > tren_pertumbuhan_loket_bulan_lalu.toInt()){
                            indikator_tren_loket?.setImageResource(R.drawable.icon_up)
                        } else {
                            indikator_tren_loket?.setImageResource(R.drawable.icon_down)
                        }

                        tren_loket_persen?.text = indikator_tren + " %"
                        tren_bulan_pertumbuhan_loket?.text = tren_pertumbuhan_loket
                        tren_pertumbuhan_loket_bulan_laluu?.text = tren_pertumbuhan_loket_bulan_lalu

                        //Set Total Jumlah Loket
                        var total_jumlah_loket = it.data_merchant.total_all_merchant.toString()

                        var total_jumlah_loket_format =  currencyformat.format(total_jumlah_loket.toLong()).toString()

                        transaksi_sukses_terbanyak?.text = total_jumlah_loket_format

                        //Set Akuisisi Loket
                        var akuisisi_admin = it.data_merchant.akuisisi_merchant.akuisisi_loket_bulan_ini[0].total_registered_by_bulan_ini.toString()
                        var akuisisi_website = it.data_merchant.akuisisi_merchant.akuisisi_loket_bulan_ini[1].total_registered_by_bulan_ini.toString()
                        var akuisisi_mobile = it.data_merchant.akuisisi_merchant.akuisisi_loket_bulan_ini[2].total_registered_by_bulan_ini.toString()

                        var akuisis_admin_bulan_lalu = it.data_merchant.akuisisi_merchant.akuisisi_loket_bulan_lalu[0].total_registered_by_bulan_lalu.toString()
                        var akuisis_website_bulan_lalu = it.data_merchant.akuisisi_merchant.akuisisi_loket_bulan_lalu[1].total_registered_by_bulan_lalu.toString()
                        var akuisis_mobile_bulan_lalu = it.data_merchant.akuisisi_merchant.akuisisi_loket_bulan_lalu[2].total_registered_by_bulan_lalu.toString()

                        akuisisi_via_admin?.text = akuisisi_admin
                        akuisisi_via_website?.text = akuisisi_website
                        akuisisi_via_mobile?.text = akuisisi_mobile

                        akuisisi_via_admin_bulan_lalu?.text = akuisis_admin_bulan_lalu
                        akuisisi_via_website_bulan_lalu?.text = akuisis_website_bulan_lalu
                        akuisisi_via_mobile_bulan_lalu?.text = akuisis_mobile_bulan_lalu
                    }
                }
            }

            override fun onFailure(call: Call<ResponseProgresLoket>, t: Throwable) {
                Log.e("pesan error","${t.message}")

                Toast.makeText(this@ProgressLoketActivity,
                    "Gagal mendapatkan data",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

    private fun loadProgresLoketFilter(){

        btn_apply_tanggal_progres_loket?.setOnClickListener {

            val loading = LoadingDialog(this)
            loading.startLoading()

            RetrofitClient.myApiClient().progresLoketFilter(startDate, endDate).enqueue(object : Callback<ResponseProgresLoketFilter>{
                override fun onResponse(
                    call: Call<ResponseProgresLoketFilter>,
                    response: Response<ResponseProgresLoketFilter>
                ) {

                    if (response.isSuccessful){
                        response.body()?.let {

                            loading.dismissLoading()

                            val currencyformat = DecimalFormat("#,###")

                            Toast.makeText(this@ProgressLoketActivity,"Berhasil filter data " + startDate + " sampai " + endDate,Toast.LENGTH_LONG).show()

                            //SET BAR CHART DATA----------------------------------------------
                            var year = it.data_merchant_filter.merchant_1_tahun[1].year.toString()
                            var bulan1 = it.data_merchant_filter.merchant_1_tahun[6].month.toString()
                            var bulan2 = it.data_merchant_filter.merchant_1_tahun[5].month.toString()
                            var bulan3 = it.data_merchant_filter.merchant_1_tahun[4].month.toString()
                            var bulan4 = it.data_merchant_filter.merchant_1_tahun[3].month.toString()
                            var bulan5 = it.data_merchant_filter.merchant_1_tahun[2].month.toString()
                            var bulan6 = it.data_merchant_filter.merchant_1_tahun[1].month.toString()
                            var bulan7 = it.data_merchant_filter.merchant_1_tahun[0].month.toString()

                            // X Axis values
                            val xValues = ArrayList<String>()
                            xValues.add(bulan1 + ", " + year)
                            xValues.add(bulan2 + ", " + year)
                            xValues.add(bulan3 + ", " + year)
                            xValues.add(bulan4 + ", " + year)
                            xValues.add(bulan5 + ", " + year)
                            xValues.add(bulan6 + ", " + year)
                            xValues.add(bulan7 + ", " + year)

                            var nilai1 = it.data_merchant_filter.merchant_1_tahun[6].data_bar_chart.toString()
                            var nilai2 = it.data_merchant_filter.merchant_1_tahun[5].data_bar_chart.toString()
                            var nilai3 = it.data_merchant_filter.merchant_1_tahun[4].data_bar_chart.toString()
                            var nilai4 = it.data_merchant_filter.merchant_1_tahun[3].data_bar_chart.toString()
                            var nilai5 = it.data_merchant_filter.merchant_1_tahun[2].data_bar_chart.toString()
                            var nilai6 = it.data_merchant_filter.merchant_1_tahun[1].data_bar_chart.toString()
                            var nilai7 = it.data_merchant_filter.merchant_1_tahun[0].data_bar_chart.toString()
                            // y axis values or bar data
                            val barEntry = ArrayList<BarEntry>()
                            barEntry.add(BarEntry(nilai1.toFloat(), 0))
                            barEntry.add(BarEntry(nilai2.toFloat(), 1))
                            barEntry.add(BarEntry(nilai3.toFloat(), 2))
                            barEntry.add(BarEntry(nilai4.toFloat(), 3))
                            barEntry.add(BarEntry(nilai5.toFloat(), 4))
                            barEntry.add(BarEntry(nilai6.toFloat(), 5))
                            barEntry.add(BarEntry(nilai7.toFloat(), 6))

                            // Bar data set
                            val barDataSet = BarDataSet(barEntry, "Bar Chart")

                            val colors = arrayListOf<Int>()
                            colors.add(ColorTemplate.rgb("#61d4b3"))
                            colors.add(ColorTemplate.rgb("#fdd365"))
                            colors.add(ColorTemplate.rgb("#f0134d"))
                            colors.add(ColorTemplate.rgb("#fb8d62"))
                            colors.add(ColorTemplate.rgb("#fd2eb3"))
                            colors.add(ColorTemplate.rgb("#ff677d"))
                            colors.add(ColorTemplate.rgb("#ff9d9d"))
                            colors.add(ColorTemplate.rgb("#633a82"))
                            colors.random()

                            barDataSet.colors = colors
                            barDataSet.valueTextSize = 10f
                            barDataSet.valueTextColor = R.color.black

                            val dataBar = BarData(xValues, barDataSet)

                            barChart.data = dataBar
                            barChart.setBackgroundColor(resources.getColor(R.color.white))
                            barChart.animateXY(1100,1100)
                            barChart.setDescription("")


                            //Set Tren Pertumbuhan Loket
                            var indikator_tren = it.data_merchant_filter.tren_loket.indikator_tren_loket.toString()
                            var tren_pertumbuhan_loket = it.data_merchant_filter.tren_loket.jumlah_tren_loket.toString()
                            var tren_pertumbuhan_loket_bulan_lalu = it.data_merchant_filter.tren_loket.jumlah_tren_loket_bulan_lalu.toString()

                            if (tren_pertumbuhan_loket.toInt() > tren_pertumbuhan_loket_bulan_lalu.toInt()){
                                indikator_tren_loket?.setImageResource(R.drawable.icon_up)
                            } else {
                                indikator_tren_loket?.setImageResource(R.drawable.icon_down)
                            }

                            tren_loket_persen?.text = indikator_tren + " %"
                            tren_bulan_pertumbuhan_loket?.text = tren_pertumbuhan_loket
                            tren_pertumbuhan_loket_bulan_laluu?.text = tren_pertumbuhan_loket_bulan_lalu

                            //Set Total Jumlah Loket
                            var total_jumlah_loket = it.data_merchant_filter.total_all_merchant.toString()

                            var total_jumlah_loket_format =  currencyformat.format(total_jumlah_loket.toLong()).toString()

                            transaksi_sukses_terbanyak?.text = total_jumlah_loket_format

                            //Set Akuisisi Loket
                            var akuisisi_admin = it.data_merchant_filter.akuisisi_merchant.akuisisi_loket_bulan_ini[0].total_registered_by_bulan_ini.toString()
                            var akuisisi_website = it.data_merchant_filter.akuisisi_merchant.akuisisi_loket_bulan_ini[1].total_registered_by_bulan_ini.toString()
                            var akuisisi_mobile = it.data_merchant_filter.akuisisi_merchant.akuisisi_loket_bulan_ini[2].total_registered_by_bulan_ini.toString()

                            var akuisisi_admin_bulan_lalu = it.data_merchant_filter.akuisisi_merchant.akuisisi_loket_bulan_lalu[0].total_registered_by_bulan_lalu.toString()
                            var akuisisi_website_bulan_lalu = it.data_merchant_filter.akuisisi_merchant.akuisisi_loket_bulan_lalu[1].total_registered_by_bulan_lalu.toString()
                            var akuisisi_mobile_bulan_lalu = it.data_merchant_filter.akuisisi_merchant.akuisisi_loket_bulan_lalu[2].total_registered_by_bulan_lalu.toString()

                            akuisisi_via_admin?.text = akuisisi_admin
                            akuisisi_via_website?.text = akuisisi_website
                            akuisisi_via_mobile?.text = akuisisi_mobile

                            akuisisi_via_admin_bulan_lalu?.text = akuisisi_admin_bulan_lalu
                            akuisisi_via_website_bulan_lalu?.text = akuisisi_website_bulan_lalu
                            akuisisi_via_mobile_bulan_lalu?.text = akuisisi_mobile_bulan_lalu
                        }
                    } else {
                        Toast.makeText(this@ProgressLoketActivity,
                            "Data filter tidak sesuai",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseProgresLoketFilter>, t: Throwable) {
                    Log.e("pesan error","${t.message}")

                    Toast.makeText(this@ProgressLoketActivity,
                        "Gagal mendapatkan data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

        }

    }

    private fun dateFrom(myCalendar: Calendar){

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerFrom.setText(sdf.format(myCalendar.time))
        startDate = sdf.format(myCalendar.time)

    }

    private fun dateTo(myCalendar: Calendar){

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerTo.setText(sdf.format(myCalendar.time))
        endDate = sdf.format(myCalendar.time)

    }

}
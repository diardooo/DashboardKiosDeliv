package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.dashboardkiosdeliv.model.ResponseDashboard
import com.example.dashboardkiosdeliv.model.ResponseDashboardFilter
import com.example.dashboardkiosdeliv.network.RetrofitClient
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_dashboard_direktur.*
import kotlinx.android.synthetic.main.activity_dashboard_staff_marketing.*
import kotlinx.android.synthetic.main.activity_informasi_deposit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DashboardStaffMarketingActivity : AppCompatActivity() {

    private lateinit var pieChart1: PieChart
    private lateinit var pieChart2: PieChart
    private lateinit var tvDatePickerFrom: TextView
    private lateinit var btnDatePickerFrom: CardView
    private lateinit var tvDatePickerTo: TextView
    private lateinit var btnDatePickerTo: CardView
    private lateinit var btnListLoketTerdampak: CardView
    private lateinit var startDate : String
    private lateinit var endDate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_staff_marketing)

        //INISIALISASI
        var btnBack = findViewById<ImageView>(R.id.img_back_dashb_staff_market)

        pieChart1 = findViewById(R.id.chart_terbanyak_staff)
        pieChart2 = findViewById(R.id.chart_gagal_staff)
        tvDatePickerFrom = findViewById(R.id.tv_date_from_staff)
        btnDatePickerFrom = findViewById(R.id.btn_date_form_staff)
        tvDatePickerTo = findViewById(R.id.tv_date_to_staff)
        btnDatePickerTo = findViewById(R.id.btn_date_to_staff)
        btnListLoketTerdampak = findViewById(R.id.btn_list_loket_terdampak)

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

        //Set Current Date on View
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)

//        tv_date_from_staff?.text = (sdf.format(Date()))
//        tv_date_to_staff?.text = (sdf.format(Date()))

        //EVENT
        btnBack.setOnClickListener {
            intent = Intent(this, MenuStaffMarketingActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnDatePickerFrom.setOnClickListener {
            DatePickerDialog(
                this, datePicker1,
                datePickerFrom.get(Calendar.YEAR),
                datePickerFrom.get(Calendar.MONTH),
                datePickerFrom.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnDatePickerTo.setOnClickListener {
            DatePickerDialog(
                this, datePicker2,
                datePickerTo.get(Calendar.YEAR),
                datePickerTo.get(Calendar.MONTH),
                datePickerTo.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
//
//        btnListLoketTerdampak.setOnClickListener {
//            startActivity(Intent(this, ListLoketTerdampakActivity::class.java))
//            finish()
//        }

        loadDashboardMarketing()
        loadDashboardMarketingFilter()

    }

    private fun loadDashboardMarketing(){

//        Handler().postDelayed({
//                Toast.makeText(this, "Sistem melakukan pemrosesan data...",Toast.LENGTH_LONG).show()
//                }, 2000)

        val loading = LoadingDialog(this)
        loading.startLoading()

        RetrofitClient.myApiClient().dashboard().enqueue(object  : Callback<ResponseDashboard>{
            override fun onResponse(
                call: Call<ResponseDashboard>,
                response: Response<ResponseDashboard>
            ) {

                if (response.isSuccessful){
                    response.body()?.let {

                        loading.dismissLoading()

                        val currencyformat = DecimalFormat("#,###")

                        tv_date_from_staff?.text =  "01 " + it.tanggal_dashboard
                        tv_date_to_staff?.text = "30 " + it.tanggal_dashboard

                        Toast.makeText(this@DashboardStaffMarketingActivity,"Menampilkan otomatis bulan " + it.tanggal_dashboard,Toast.LENGTH_LONG).show()

                        //SET TOTAL TRANSAKSI --------------------------------------------------------------------------------
                        var indikator_total_transaksi = it.data_dashboard.indikator
                        var total_transaksi = it.data_dashboard.total_transaksi
                        var total_transaksi_bulan_lalu = it.data_dashboard.total_transaksi_bulan_lalu

                        if (total_transaksi > total_transaksi_bulan_lalu){
                            indikator_total_transaksiii?.setImageResource(R.drawable.icon_up)
                        } else {
                            indikator_total_transaksiii?.setImageResource(R.drawable.icon_down)
                        }

                        var total_transaksi_format = currencyformat.format(total_transaksi.toLong())
                        var total_transaksi_bulan_lalu_format = currencyformat.format((total_transaksi_bulan_lalu.toLong()))

                        total_transaksi_persen_marketing?.text = indikator_total_transaksi + " %"
                        total_transaksi_marketing?.text = total_transaksi_format.toString()
                        total_transaksi_bulan_lalu_marketing?.text = total_transaksi_bulan_lalu_format.toString()

                        //SET PIE CHART DATA 1 --------------------------------------------------------------------------------
                        var service1 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[0].name
                        var service2 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[1].name
                        var service3 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[2].name
                        var service4 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[3].name
                        var service5 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[4].name

                        val xValues = ArrayList<String>()
                        xValues.add(service1)
                        xValues.add(service2)
                        xValues.add(service3)
                        xValues.add(service4)
                        xValues.add(service5)

                        var nilai1 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[0].total
                        var nilai2 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[1].total
                        var nilai3 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[2].total
                        var nilai4 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[3].total
                        var nilai5 = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses[4].total

                        val pieChartEntry = ArrayList<Entry>()
                        pieChartEntry.add( Entry(nilai1.toFloat(), 0 ))
                        pieChartEntry.add( Entry(nilai2.toFloat(), 1 ))
                        pieChartEntry.add( Entry(nilai3.toFloat(), 2 ))
                        pieChartEntry.add( Entry(nilai4.toFloat(), 3 ))
                        pieChartEntry.add( Entry(nilai5.toFloat(), 4 ))

                        // SETUP PIE CHART ANIMATION
                        pieChart1.animateXY(1000, 1000)

                        // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                        val pieDataSet = PieDataSet(pieChartEntry,"")
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

                        pieDataSet.colors = colors
                        pieDataSet.sliceSpace = 1f
                        pieDataSet.valueTextSize = 8f
                        pieDataSet.valueTextColor = R.color.black

                        // SETUP TEXT IN PIE CHART CENTER
                        pieChart1.centerText = "Transaksi Berhasil"
                        pieChart1.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
                        pieChart1.setCenterTextSize(10F)

                        // SETUP PIE DATA SET IN PieData
                        val pieData = PieData( xValues,pieDataSet)

                        // ENABLED THE VALUE ON EACH PieChartEntry
                        pieData.setDrawValues(true)
                        pieChart1.data = pieData

                        // OPTIONAL PIE CHART SETUP
                        pieChart1.holeRadius = 40f
                        pieChart1.setBackgroundColor(resources.getColor(R.color.white))
                        pieChart1.transparentCircleRadius = 60f
                        pieChart1.setDescription("")
                        pieChart1.setExtraOffsets(5f, 10f, 5f, 5f)

                        //SET PIE CHART DETAILS DATA 1 --------------------------------------------------------------------------------

                        var indikator_service_sukses_terbanyak = it.data_dashboard.transaksi_service_complete.indikator_total_transaksi_service_sukses + " %"
                        var service_sukses_terbanyak = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.name
                        var nilai_service_sukses_terbanyak = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.total
                        var nilai_service_sukses_terbanyak_bulan_lalu = it.data_dashboard.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.bulan_lalu

                        if (nilai_service_sukses_terbanyak > nilai_service_sukses_terbanyak_bulan_lalu){
                            indikator_transaksi_suksesss.setImageResource(R.drawable.icon_up)
                        } else{
                            indikator_transaksi_suksesss.setImageResource(R.drawable.icon_down)
                        }

                        nama_transaksi_sukses_terbanyak_marketing?.text = service_sukses_terbanyak
                        transaksi_sukses_persen_marketing?.text = indikator_service_sukses_terbanyak
                        transaksi_sukses_terbanyak_marketing?.text = currencyformat.format(nilai_service_sukses_terbanyak).toString()
                        transaksi_sukses_bulan_lalu_marketing?.text = currencyformat.format(nilai_service_sukses_terbanyak_bulan_lalu).toString()

                        //SET PIE CHART DATA 2 --------------------------------------------------------------------------------
                        var service11 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[0].name
                        var service22 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[1].name
                        var service33 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[2].name
                        var service44 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[3].name
                        var service55 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[4].name

                        val xValues2 = ArrayList<String >()
                        xValues2.add(service11)
                        xValues2.add(service22)
                        xValues2.add(service33)
                        xValues2.add(service44)
                        xValues2.add(service55)

                        var nilai11 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[0].total
                        var nilai22 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[1].total
                        var nilai33 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[2].total
                        var nilai44 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[3].total
                        var nilai55 = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal[4].total

                        val pieChartEntry2 = ArrayList<Entry>()
                        pieChartEntry2.add( Entry(nilai11.toFloat(), 0))
                        pieChartEntry2.add( Entry(nilai22.toFloat(), 1 ))
                        pieChartEntry2.add( Entry(nilai33.toFloat(), 2 ))
                        pieChartEntry2.add( Entry(nilai44.toFloat(), 3 ))
                        pieChartEntry2.add( Entry(nilai55.toFloat(), 4 ))

                        // SETUP PIE CHART ANIMATION
                        pieChart2.animateXY(1000, 1000)

                        // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                        val pieDataSet2 = PieDataSet(pieChartEntry2,"")
                        val colors2 = arrayListOf<Int>()
                        colors2.add(ColorTemplate.rgb("#f0134d"))
                        colors2.add(ColorTemplate.rgb("#fb8d62"))
                        colors2.add(ColorTemplate.rgb("#fd2eb3"))
                        colors2.add(ColorTemplate.rgb("#ff677d"))
                        colors2.add(ColorTemplate.rgb("#ff9d9d"))
                        colors2.add(ColorTemplate.rgb("#633a82"))
                        colors2.add(ColorTemplate.rgb("#61d4b3"))
                        colors2.add(ColorTemplate.rgb("#fdd365"))
                        colors2.random()

                        pieDataSet2.colors = colors2
                        pieDataSet2.sliceSpace = 1f
                        pieDataSet2.valueTextSize = 8f
                        pieDataSet2.valueTextColor = R.color.black

                        // SETUP TEXT IN PIE CHART CENTER
                        pieChart2.centerText = "Transaksi Gagal"
                        pieChart2.setCenterTextColor(resources.getColor(android.R.color.holo_red_dark))
                        pieChart2.setCenterTextSize(10F)

                        // SETUP PIE DATA SET IN PieData
                        val pieData2 = PieData(xValues2,pieDataSet2)

                        // ENABLED THE VALUE ON EACH PieChartEntry
                        pieData2.setDrawValues(true)
                        pieChart2.data = pieData2

                        // OPTIONAL PIE CHART SETUP
                        pieChart2.holeRadius = 40f
                        pieChart2.setBackgroundColor(resources.getColor(R.color.white))
                        pieChart2.transparentCircleRadius = 60f
                        pieChart2.setDescription("")
                        pieChart2.setExtraOffsets(5f, 10f, 5f, 5f)

                        //SET PIE CHART DETAILS DATA 2 --------------------------------------------------------------------------------
                        var indikator_service_gagal_terbanyak = it.data_dashboard.transaksi_service_failed.indikator_total_transaksi_service_failed + " %"
                        var nama_service_gagal_terbanyak = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.name
                        var nilai_service_gagal_terbanyak = currencyformat.format(it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.total)
                        var nilai_service_gagal_terbanyak_bulan_lalu = currencyformat.format(it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.bulan_lalu)

                        if (nilai_service_gagal_terbanyak > nilai_service_gagal_terbanyak_bulan_lalu){
                            indikator_transaksi_gagalll.setImageResource(R.drawable.icon_down)
                        } else {
                            indikator_transaksi_gagalll.setImageResource(R.drawable.icon_up)
                        }

                        transaksi_gagal_persen_marketing?.text = indikator_service_gagal_terbanyak
                        nama_transaksi_gagal_terbanyak_marketing?.text = nama_service_gagal_terbanyak
                        transaksi_gagal_terbanyak_marketing?.text = nilai_service_gagal_terbanyak.toString()
                        transaksi_gagal_bulan_lalu_marketing?.text = nilai_service_gagal_terbanyak_bulan_lalu.toString()

                    }
                }

            }

            override fun onFailure(call: Call<ResponseDashboard>, t: Throwable) {
                Log.e("pesan error","${t.message}")

                Toast.makeText(this@DashboardStaffMarketingActivity,
                    "Gagal mendapatkan data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun loadDashboardMarketingFilter(){

        btn_apply_tanggal_marketing?.setOnClickListener {

//            Handler().postDelayed({
//                Toast.makeText(this, "Sistem melakukan pemrosesan data...",Toast.LENGTH_LONG).show()
//                }, 2000)
            val loading = LoadingDialog(this)
            loading.startLoading()

            RetrofitClient.myApiClient().dashboardFilter(startDate,endDate).enqueue(object  : Callback<ResponseDashboardFilter>{
                override fun onResponse(
                    call: Call<ResponseDashboardFilter>,
                    response: Response<ResponseDashboardFilter>
                ) {

                    if (response.isSuccessful){
                        response.body()?.let {

                            loading.dismissLoading()

                            val currencyformat = DecimalFormat("#,###")

                            Toast.makeText(this@DashboardStaffMarketingActivity,"Berhasil filter data " + startDate + " sampai " + endDate,Toast.LENGTH_LONG).show()

                            //SET TOTAL TRANSAKSI --------------------------------------------------------------------------------
                            var indikator_total_transaksi = it.data_dashboard_filter.indikator
                            var total_transaksi = it.data_dashboard_filter.total_transaksi
                            var total_transaksi_bulan_lalu = it.data_dashboard_filter.total_transaksi_bulan_lalu

                            if (total_transaksi > total_transaksi_bulan_lalu){
                                indikator_total_transaksiii?.setImageResource(R.drawable.icon_up)
                            } else {
                                indikator_total_transaksiii?.setImageResource(R.drawable.icon_down)
                            }

                            var total_transaksi_format = currencyformat.format(total_transaksi.toLong())
                            var total_transaksi_bulan_lalu_format = currencyformat.format((total_transaksi_bulan_lalu.toLong()))

                            total_transaksi_persen_marketing?.text = indikator_total_transaksi + " %"
                            total_transaksi_marketing?.text = total_transaksi_format.toString()
                            total_transaksi_bulan_lalu_marketing?.text = total_transaksi_bulan_lalu_format.toString()

                            //SET PIE CHART DATA 1 --------------------------------------------------------------------------------
                            var service1 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[0].name
                            var service2 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[1].name
                            var service3 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[2].name
                            var service4 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[3].name
                            var service5 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[4].name

                            val xValues = ArrayList<String>()
                            xValues.add(service1)
                            xValues.add(service2)
                            xValues.add(service3)
                            xValues.add(service4)
                            xValues.add(service5)

                            var nilai1 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[0].total
                            var nilai2 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[1].total
                            var nilai3 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[2].total
                            var nilai4 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[3].total
                            var nilai5 = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses[4].total

                            val pieChartEntry = ArrayList<Entry>()
                            pieChartEntry.add( Entry(nilai1.toFloat(), 0 ))
                            pieChartEntry.add( Entry(nilai2.toFloat(), 1 ))
                            pieChartEntry.add( Entry(nilai3.toFloat(), 2 ))
                            pieChartEntry.add( Entry(nilai4.toFloat(), 3 ))
                            pieChartEntry.add( Entry(nilai5.toFloat(), 4 ))

                            // SETUP PIE CHART ANIMATION
                            pieChart1.animateXY(1000, 1000)

                            // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                            val pieDataSet = PieDataSet(pieChartEntry,"")
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

                            pieDataSet.colors = colors
                            pieDataSet.sliceSpace = 1f
                            pieDataSet.valueTextSize = 8f
                            pieDataSet.valueTextColor = R.color.black

                            // SETUP TEXT IN PIE CHART CENTER
                            pieChart1.centerText = "Transaksi Berhasil"
                            pieChart1.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
                            pieChart1.setCenterTextSize(10F)

                            // SETUP PIE DATA SET IN PieData
                            val pieData = PieData( xValues,pieDataSet)

                            // ENABLED THE VALUE ON EACH PieChartEntry
                            pieData.setDrawValues(true)
                            pieChart1.data = pieData

                            // OPTIONAL PIE CHART SETUP
                            pieChart1.holeRadius = 40f
                            pieChart1.setBackgroundColor(resources.getColor(R.color.white))
                            pieChart1.transparentCircleRadius = 60f
                            pieChart1.setDescription("")
                            pieChart1.setExtraOffsets(5f, 10f, 5f, 5f)

                            //SET PIE CHART DETAILS DATA 1 --------------------------------------------------------------------------------

                            var indikator_service_sukses_terbanyak = it.data_dashboard_filter.transaksi_service_complete.indikator_total_transaksi_service_sukses + " %"
                            var service_sukses_terbanyak = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.name
                            var nilai_service_sukses_terbanyak = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.total
                            var nilai_service_sukses_terbanyak_bulan_lalu = it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.bulan_lalu

                            if (nilai_service_sukses_terbanyak > nilai_service_sukses_terbanyak_bulan_lalu){
                                indikator_transaksi_suksesss.setImageResource(R.drawable.icon_up)
                            } else{
                                indikator_transaksi_suksesss.setImageResource(R.drawable.icon_down)
                            }

                            nama_transaksi_sukses_terbanyak_marketing?.text = service_sukses_terbanyak
                            transaksi_sukses_persen_marketing?.text = indikator_service_sukses_terbanyak
                            transaksi_sukses_terbanyak_marketing?.text = currencyformat.format(nilai_service_sukses_terbanyak).toString()
                            transaksi_sukses_bulan_lalu_marketing?.text = currencyformat.format(nilai_service_sukses_terbanyak_bulan_lalu).toString()


                            //SET PIE CHART DATA 2 --------------------------------------------------------------------------------
                            var service11 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[0].name
                            var service22 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[1].name
                            var service33 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[2].name
                            var service44 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[3].name
                            var service55 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[4].name

                            val xValues2 = ArrayList<String >()
                            xValues2.add(service11)
                            xValues2.add(service22)
                            xValues2.add(service33)
                            xValues2.add(service44)
                            xValues2.add(service55)

                            var nilai11 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[0].total
                            var nilai22 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[1].total
                            var nilai33 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[2].total
                            var nilai44 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[3].total
                            var nilai55 = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal[4].total

                            val pieChartEntry2 = ArrayList<Entry>()
                            pieChartEntry2.add( Entry(nilai11.toFloat(), 0))
                            pieChartEntry2.add( Entry(nilai22.toFloat(), 1 ))
                            pieChartEntry2.add( Entry(nilai33.toFloat(), 2 ))
                            pieChartEntry2.add( Entry(nilai44.toFloat(), 3 ))
                            pieChartEntry2.add( Entry(nilai55.toFloat(), 4 ))

                            // SETUP PIE CHART ANIMATION
                            pieChart2.animateXY(1000, 1000)

                            // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                            val pieDataSet2 = PieDataSet(pieChartEntry2,"")
                            val colors2 = arrayListOf<Int>()
                            colors2.add(ColorTemplate.rgb("#f0134d"))
                            colors2.add(ColorTemplate.rgb("#fb8d62"))
                            colors2.add(ColorTemplate.rgb("#fd2eb3"))
                            colors2.add(ColorTemplate.rgb("#ff677d"))
                            colors2.add(ColorTemplate.rgb("#ff9d9d"))
                            colors2.add(ColorTemplate.rgb("#633a82"))
                            colors2.add(ColorTemplate.rgb("#61d4b3"))
                            colors2.add(ColorTemplate.rgb("#fdd365"))
                            colors2.random()

                            pieDataSet2.colors = colors2
                            pieDataSet2.sliceSpace = 1f
                            pieDataSet2.valueTextSize = 8f
                            pieDataSet2.valueTextColor = R.color.black

                            // SETUP TEXT IN PIE CHART CENTER
                            pieChart2.centerText = "Transaksi Gagal"
                            pieChart2.setCenterTextColor(resources.getColor(android.R.color.holo_red_dark))
                            pieChart2.setCenterTextSize(10F)

                            // SETUP PIE DATA SET IN PieData
                            val pieData2 = PieData(xValues2,pieDataSet2)

                            // ENABLED THE VALUE ON EACH PieChartEntry
                            pieData2.setDrawValues(true)
                            pieChart2.data = pieData2

                            // OPTIONAL PIE CHART SETUP
                            pieChart2.holeRadius = 40f
                            pieChart2.setBackgroundColor(resources.getColor(R.color.white))
                            pieChart2.transparentCircleRadius = 60f
                            pieChart2.setDescription("")
                            pieChart2.setExtraOffsets(5f, 10f, 5f, 5f)

                            //SET PIE CHART DETAILS DATA 2 --------------------------------------------------------------------------------
                            var indikator_service_gagal_terbanyak = it.data_dashboard_filter.transaksi_service_failed.indikator_total_transaksi_service_failed + " %"
                            var nama_service_gagal_terbanyak = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.name
                            var nilai_service_gagal_terbanyak = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.total
                            var nilai_service_gagal_terbanyak_bulan_lalu = it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.bulan_lalu

                            if (nilai_service_gagal_terbanyak > nilai_service_gagal_terbanyak_bulan_lalu){
                                indikator_transaksi_gagalll.setImageResource(R.drawable.icon_down)
                            } else {
                                indikator_transaksi_gagalll.setImageResource(R.drawable.icon_up)
                            }

                            transaksi_gagal_persen_marketing?.text = indikator_service_gagal_terbanyak
                            nama_transaksi_gagal_terbanyak_marketing?.text = nama_service_gagal_terbanyak
                            transaksi_gagal_terbanyak_marketing?.text = currencyformat.format(nilai_service_gagal_terbanyak).toString()
                            transaksi_gagal_bulan_lalu_marketing?.text = currencyformat.format(nilai_service_gagal_terbanyak_bulan_lalu).toString()

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseDashboardFilter>, t: Throwable) {
                    Log.e("pesan error","${t.message}")

                    Toast.makeText(this@DashboardStaffMarketingActivity,
                        "Gagal mendapatkan data",
                        Toast.LENGTH_SHORT
                    ).show()            }
            })

        }
    }

    private fun dateFrom(myCalendar: Calendar) {

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerFrom.setText(sdf.format(myCalendar.time))
        startDate = sdf.format(myCalendar.time)
    }

    private fun dateTo(myCalendar: Calendar) {

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerTo.setText(sdf.format(myCalendar.time))
        endDate = sdf.format(myCalendar.time)
    }
}
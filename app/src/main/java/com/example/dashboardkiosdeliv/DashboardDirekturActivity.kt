package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.dashboardkiosdeliv.model.ResponseDashboard
import com.example.dashboardkiosdeliv.model.ResponseDashboardFilter
import com.example.dashboardkiosdeliv.network.RetrofitClient
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_dashboard_direktur.*
import kotlinx.android.synthetic.main.activity_dashboard_direktur.transaksi_sukses_terbanyak
import kotlinx.android.synthetic.main.activity_dashboard_staff_marketing.*
import kotlinx.android.synthetic.main.activity_informasi_deposit.*
import kotlinx.android.synthetic.main.activity_progress_loket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardDirekturActivity : AppCompatActivity() {

    private lateinit var pieChart1 : PieChart
    private lateinit var pieChart2 : PieChart
    private lateinit var pieChartService : PieChart
    private lateinit var pieChartVendor : PieChart
    private lateinit var tvDatePickerFrom : TextView
    private lateinit var btnDatePickerFrom : CardView
    private lateinit var tvDatePickerTo : TextView
    private lateinit var btnDatePickerTo : CardView
    private lateinit var startDate : String
    private lateinit var endDate : String

    private lateinit var indikator : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_direktur)

        //INISIALISASI
        var btnBack = findViewById<ImageView>(R.id.img_back_dashb_direktur)

        pieChart1 = findViewById(R.id.chart_terbanyak)
        pieChart2 = findViewById(R.id.chart_gagal)
        pieChartService = findViewById(R.id.chart_service)
        pieChartVendor = findViewById(R.id.chart_vendor)
        tvDatePickerFrom = findViewById(R.id.tv_date_from_dsb_direktur)
        btnDatePickerFrom = findViewById(R.id.btn_date_form_dsb_direktur)
        tvDatePickerTo = findViewById(R.id.tv_date_to_dsb_direktur)
        btnDatePickerTo = findViewById(R.id.btn_date_to_dsb_direktur)

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
            intent = Intent(this, MenuDirekturActivity::class.java)
            startActivity(intent)
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

        btn_expand1.setOnClickListener {
            if(expandLayout1.visibility == View.GONE){
                TransitionManager.beginDelayedTransition(cardview1, AutoTransition())
                expandLayout1.visibility = View.VISIBLE
                btn_expand1.text = "COLLAPSE"
            } else {
                TransitionManager.beginDelayedTransition(cardview1, AutoTransition())
                expandLayout1.visibility = View.GONE
                btn_expand1.text = "EXPAND"
            }
        }

        btn_expand2.setOnClickListener {
            if(expandLayout2.visibility == View.GONE){
                TransitionManager.beginDelayedTransition(cardview2, AutoTransition())
                expandLayout2.visibility = View.VISIBLE
                btn_expand2.text = "COLLAPSE"
            } else {
                TransitionManager.beginDelayedTransition(cardview2, AutoTransition())
                expandLayout2.visibility = View.GONE
                btn_expand2.text = "EXPAND"
            }
        }

        //Set Current Date on View
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)

//        tv_date_from_dsb_direktur?.text = (sdf.format(Date()))
//        tv_date_to_dsb_direktur?.text = (sdf.format(Date()))

        loadDashboard()
        loadDashboardFilter()

    }

    private fun loadDashboard(){

//        Handler().postDelayed({
//                Toast.makeText(this, "Sistem melakukan pemrosesan data...",Toast.LENGTH_LONG).show()
//                }, 2000)

        val loading = LoadingDialog(this)
        loading.startLoading()

        RetrofitClient.myApiClient().dashboard().enqueue(object : Callback<ResponseDashboard>{
            override fun onResponse(
                call: Call<ResponseDashboard>,
                response: Response<ResponseDashboard>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {

                        loading.dismissLoading()

                        val currencyformat = DecimalFormat("#,###")

                        tv_date_from_dsb_direktur?.text =  "01 " + it.tanggal_dashboard
                        tv_date_to_dsb_direktur?.text = "30 " + it.tanggal_dashboard

                        Toast.makeText(this@DashboardDirekturActivity,"Menampilkan otomatis bulan " + it.tanggal_dashboard,Toast.LENGTH_LONG).show()

                        //SET TOTAL TRANSAKSI --------------------------------------------------------------------------------
                        var indikator_total_transaksi = it.data_dashboard.indikator
                        var total_transaksi = it.data_dashboard.total_transaksi
                        var total_transaksi_bulan_lalu = it.data_dashboard.total_transaksi_bulan_lalu

                        if (total_transaksi > total_transaksi_bulan_lalu){
                            indikator_total_transaksii?.setImageResource(R.drawable.icon_up)
                        } else {
                            indikator_total_transaksii?.setImageResource(R.drawable.icon_down)
                        }

                        var total_transaksi_format = currencyformat.format(total_transaksi.toLong())
                        var total_transaksi_bulan_lalu_format = currencyformat.format((total_transaksi_bulan_lalu.toLong()))

                        total_transaksi_persen?.text = indikator_total_transaksi + " %"
                        total_transaksii?.text = total_transaksi_format.toString()
                        total_transaksi_bulan_laluu?.text = total_transaksi_bulan_lalu_format.toString()

                        //SET PENDAPATAN TIAP SERVICE
                        var nama_service1 = it.data_dashboard.total_uang_service[0].name
                        var nama_service2 = it.data_dashboard.total_uang_service[1].name
                        var nama_service3 = it.data_dashboard.total_uang_service[2].name
                        var nama_service4 = it.data_dashboard.total_uang_service[3].name
                        var nama_service5 = it.data_dashboard.total_uang_service[4].name

                        var service_nilai1 = it.data_dashboard.total_uang_service[0].total_pendapatan
                        var service_nilai2 = it.data_dashboard.total_uang_service[1].total_pendapatan
                        var service_nilai3 = it.data_dashboard.total_uang_service[2].total_pendapatan
                        var service_nilai4 = it.data_dashboard.total_uang_service[3].total_pendapatan
                        var service_nilai5 = it.data_dashboard.total_uang_service[4].total_pendapatan

                        var service_nilai1_format = currencyformat.format(service_nilai1.toLong())
                        var service_nilai2_format = currencyformat.format(service_nilai2.toLong())
                        var service_nilai3_format = currencyformat.format(service_nilai3.toLong())
                        var service_nilai4_format = currencyformat.format(service_nilai4.toLong())
                        var service_nilai5_format = currencyformat.format(service_nilai5.toLong())

                        service1?.text = nama_service1
                        service2?.text = nama_service2
                        service3?.text = nama_service3
                        service4?.text = nama_service4
                        service5?.text = nama_service5

                        nilai_service1?.text = "Rp " + service_nilai1_format.toString()
                        nilai_service2?.text = "Rp " + service_nilai2_format.toString()
                        nilai_service3?.text = "Rp " + service_nilai3_format.toString()
                        nilai_service4?.text = "Rp " + service_nilai4_format.toString()
                        nilai_service5?.text = "Rp " + service_nilai5_format.toString()

                        //SET PIE CHART DATA SERVICE --------------------------------------------------------------------------------
                        var nama_service11 = it.data_dashboard.total_uang_service[0].name
                        var nama_service22 = it.data_dashboard.total_uang_service[1].name
                        var nama_service33 = it.data_dashboard.total_uang_service[2].name
                        var nama_service44 = it.data_dashboard.total_uang_service[3].name
                        var nama_service55 = it.data_dashboard.total_uang_service[4].name

                        val xValuesService = ArrayList<String>()
                        xValuesService.add(nama_service11)
                        xValuesService.add(nama_service22)
                        xValuesService.add(nama_service33)
                        xValuesService.add(nama_service44)
                        xValuesService.add(nama_service55)

                        var service_nilai11 = it.data_dashboard.total_uang_service[0].total_pendapatan
                        var service_nilai22 = it.data_dashboard.total_uang_service[1].total_pendapatan
                        var service_nilai33 = it.data_dashboard.total_uang_service[2].total_pendapatan
                        var service_nilai44 = it.data_dashboard.total_uang_service[3].total_pendapatan
                        var service_nilai55 = it.data_dashboard.total_uang_service[4].total_pendapatan

                        val pieChartEntryService = ArrayList<Entry>()
                        pieChartEntryService.add( Entry(service_nilai11.toFloat(), 0 ))
                        pieChartEntryService.add( Entry(service_nilai22.toFloat(), 1 ))
                        pieChartEntryService.add( Entry(service_nilai33.toFloat(), 2 ))
                        pieChartEntryService.add( Entry(service_nilai44.toFloat(), 3 ))
                        pieChartEntryService.add( Entry(service_nilai55.toFloat(), 4 ))

                        // SETUP PIE CHART ANIMATION
                        pieChartService.animateXY(1000, 1000)

                        // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                        val pieDataSetService = PieDataSet(pieChartEntryService,"")
                        val colorsService = arrayListOf<Int>()
                        colorsService.add(ColorTemplate.rgb("#61d4b3"))
                        colorsService.add(ColorTemplate.rgb("#fdd365"))
                        colorsService.add(ColorTemplate.rgb("#f0134d"))
                        colorsService.add(ColorTemplate.rgb("#fd2eb3"))
                        colorsService.add(ColorTemplate.rgb("#ff677d"))
                        colorsService.add(ColorTemplate.rgb("#ff9d9d"))
                        colorsService.add(ColorTemplate.rgb("#633a82"))
                        colorsService.random()

                        pieDataSetService.colors = colorsService
                        pieDataSetService.sliceSpace = 1f
                        pieDataSetService.valueTextSize = 7f
                        pieDataSetService.valueTextColor = R.color.grayMuda

                        // SETUP TEXT IN PIE CHART CENTER
                        pieChartService.centerText = "Pendapatan Tiap Service"
                        pieChartService.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
                        pieChartService.setCenterTextSize(9F)

                        // SETUP PIE DATA SET IN PieData
                        val pieDataService = PieData( xValuesService,pieDataSetService)

                        // ENABLED THE VALUE ON EACH PieChartEntry
                        pieDataService.setDrawValues(true)
                        pieChartService.data = pieDataService

                        // OPTIONAL PIE CHART SETUP
                        pieChartService.holeRadius = 35f
                        pieChartService.setBackgroundColor(resources.getColor(R.color.gray))
                        pieChartService.transparentCircleRadius = 55f
                        pieChartService.setDescription("")
                        pieChartService.setExtraOffsets(5f, 10f, 5f, 5f)

                        //SET LEGEND CUSTOMIZATION
                        val legend: Legend = pieChartService.legend
                        legend.textColor = resources.getColor(R.color.white)
                        legend.position = Legend.LegendPosition.BELOW_CHART_LEFT
                        legend.textSize = 8f

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
                            indikator_transaksi_sukses.setImageResource(R.drawable.icon_up)
                        } else{
                            indikator_transaksi_sukses.setImageResource(R.drawable.icon_down)
                        }

                        nama_transaksi_sukses_terbanyak?.text = service_sukses_terbanyak
                        transaksi_sukses_persen?.text = indikator_service_sukses_terbanyak
                        transaksi_sukses_terbanyak?.text = currencyformat.format(nilai_service_sukses_terbanyak).toString()
                        transaksi_sukses_bulan_lalu?.text = currencyformat.format(nilai_service_sukses_terbanyak_bulan_lalu).toString()

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
                        var nilai_service_gagal_terbanyak = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.total
                        var nilai_service_gagal_terbanyak_bulan_lalu = it.data_dashboard.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.bulan_lalu

                        if (nilai_service_gagal_terbanyak > nilai_service_gagal_terbanyak_bulan_lalu){
                            indikator_transaksi_gagal.setImageResource(R.drawable.icon_down)
                        } else {
                            indikator_transaksi_gagal.setImageResource(R.drawable.icon_up)
                        }

                        transaksi_gagal_persen?.text = indikator_service_gagal_terbanyak
                        nama_transaksi_gagal_terbanyak?.text = nama_service_gagal_terbanyak
                        transaksi_gagal_terbanyak?.text = currencyformat.format(nilai_service_gagal_terbanyak).toString()
                        transaksi_gagal_bulan_lalu?.text = currencyformat.format(nilai_service_gagal_terbanyak_bulan_lalu).toString()

                        //SET PENDAPATAN KIOS DELIV --------------------------------------------------------------------------------
                        var indikator_pendapatan_kotor = it.data_dashboard.total_pendapatan.indikator_total_pendapatan_kotor + " %"
                        var nilai_pendapatan_kotor = it.data_dashboard.total_pendapatan.total_pendapatan_kotor.toInt()
                        var nilai_pendapatan_kotor_bulan_lalu = it.data_dashboard.total_pendapatan.total_pendapatan_kotor_bulan_lalu.toInt()

                        if (nilai_pendapatan_kotor > nilai_pendapatan_kotor_bulan_lalu) {
                            indikator_total_pendapatan_kotor.setImageResource(R.drawable.icon_up)
                        } else {
                            indikator_total_pendapatan_kotor.setImageResource(R.drawable.icon_down)
                        }

                        var indikator_komisi = it.data_dashboard.total_pendapatan.indikator_total_komisi + " %"
                        var nilai_komisi = it.data_dashboard.total_pendapatan.total_komisi.toInt()
                        var nilai_komisi_bulan_lalu = it.data_dashboard.total_pendapatan.total_komisi_bulan_lalu.toInt()

                        if (nilai_komisi > nilai_komisi_bulan_lalu) {
                            indikator_total_komisi.setImageResource(R.drawable.icon_up)
                        } else {
                            indikator_total_komisi.setImageResource(R.drawable.icon_down)
                        }

                        var indikator_pendapatan_bersih = it.data_dashboard.total_pendapatan.indikator_total_pendapatan_bersih + " %"
                        var nilai_pendapatan_bersih = it.data_dashboard.total_pendapatan.total_pendapatan_bersih.toInt()
                        var nilai_pendapatan_bersih_bulan_lalu = it.data_dashboard.total_pendapatan.total_pendapatan_bersih_bulan_lalu.toInt()

                        if (nilai_pendapatan_bersih > nilai_pendapatan_bersih_bulan_lalu) {
                            indikator_total_pendapatan_bersih.setImageResource(R.drawable.icon_up)
                        } else {
                            indikator_total_pendapatan_bersih.setImageResource(R.drawable.icon_down)
                        }

                        //FORMAT
                        var nilai_pendapatan_kotor_format = "Rp " + currencyformat.format(nilai_pendapatan_kotor.toLong()).toString()
                        var nilai_pendapatan_kotor_bulan_lalu_format = "Rp " + currencyformat.format(nilai_pendapatan_kotor_bulan_lalu.toLong()).toString()

                        var nilai_komisi_format = "Rp " + currencyformat.format(nilai_komisi.toLong()).toString()
                        var nilai_komisi_bulan_lalu_format = "Rp " + currencyformat.format(nilai_komisi_bulan_lalu.toLong()).toString()

                        var nilai_pendapatan_bersih_format = "Rp " + currencyformat.format(nilai_pendapatan_bersih.toLong()).toString()
                        var nilai_pendapataan_bersih_bulan_lalu_format = "Rp " + currencyformat.format(nilai_pendapatan_bersih_bulan_lalu.toLong()).toString()

                        total_pendapatan_kotor_persen?.text = indikator_pendapatan_kotor
                        total_pendapatan_kotor?.text = nilai_pendapatan_kotor_format
                        total_pendapatan_kotor_bulan_lalu?.text = nilai_pendapatan_kotor_bulan_lalu_format

                        total_komisi_persen?.text = indikator_komisi
                        total_komisi?.text = nilai_komisi_format
                        total_komisi_bulan_lalu?.text = nilai_komisi_bulan_lalu_format

                        total_pendapatan_bersih_persen?.text = indikator_pendapatan_bersih
                        total_pendapatan_bersih?.text = nilai_pendapatan_bersih_format
                        total_pendapatan_bersih_bulan_lalu?.text = nilai_pendapataan_bersih_bulan_lalu_format

                        //SET PENDAPATAN TIAP VENDOR
                        var nama_vendor1 = it.data_dashboard.total_uang_vendor[0].name
                        var nama_vendor2 = it.data_dashboard.total_uang_vendor[1].name
                        var nama_vendor3 = it.data_dashboard.total_uang_vendor[2].name
                        var nama_vendor4 = it.data_dashboard.total_uang_vendor[3].name
                        var nama_vendor5 = it.data_dashboard.total_uang_vendor[4].name

                        var vendor_nilai1 = it.data_dashboard.total_uang_vendor[0].total_pendapatan
                        var vendor_nilai2 = it.data_dashboard.total_uang_vendor[1].total_pendapatan
                        var vendor_nilai3 = it.data_dashboard.total_uang_vendor[2].total_pendapatan
                        var vendor_nilai4 = it.data_dashboard.total_uang_vendor[3].total_pendapatan
                        var vendor_nilai5 = it.data_dashboard.total_uang_vendor[4].total_pendapatan

                        var vendor_nilai1_format = currencyformat.format(vendor_nilai1.toLong())
                        var vendor_nilai2_format = currencyformat.format(vendor_nilai2.toLong())
                        var vendor_nilai3_format = currencyformat.format(vendor_nilai3.toLong())
                        var vendor_nilai4_format = currencyformat.format(vendor_nilai4.toLong())
                        var vendor_nilai5_format = currencyformat.format(vendor_nilai5.toLong())

                        vendor1?.text = nama_vendor1
                        vendor2?.text = nama_vendor2
                        vendor3?.text = nama_vendor3
                        vendor4?.text = nama_vendor4
                        vendor5?.text = nama_vendor5

                        nilai_vendor1?.text = "Rp " + vendor_nilai1_format.toString()
                        nilai_vendor2?.text = "Rp " + vendor_nilai2_format.toString()
                        nilai_vendor3?.text = "Rp " + vendor_nilai3_format.toString()
                        nilai_vendor4?.text = "Rp " + vendor_nilai4_format.toString()
                        nilai_vendor5?.text = "Rp " + vendor_nilai5_format.toString()

                        //SET PIE CHART DATA VENDOR --------------------------------------------------------------------------------
                        var nama_vendor11 = it.data_dashboard.total_uang_vendor[0].name
                        var nama_vendor22 = it.data_dashboard.total_uang_vendor[1].name
                        var nama_vendor33 = it.data_dashboard.total_uang_vendor[2].name
                        var nama_vendor44 = it.data_dashboard.total_uang_vendor[3].name
                        var nama_vendor55 = it.data_dashboard.total_uang_vendor[4].name

                        val xValuesVendor = ArrayList<String>()
                        xValuesVendor.add(nama_vendor11)
                        xValuesVendor.add(nama_vendor22)
                        xValuesVendor.add(nama_vendor33)
                        xValuesVendor.add(nama_vendor44)
                        xValuesVendor.add(nama_vendor55)

                        var vendor_nilai11 = it.data_dashboard.total_uang_vendor[0].total_pendapatan
                        var vendor_nilai22 = it.data_dashboard.total_uang_vendor[1].total_pendapatan
                        var vendor_nilai33 = it.data_dashboard.total_uang_vendor[2].total_pendapatan
                        var vendor_nilai44 = it.data_dashboard.total_uang_vendor[3].total_pendapatan
                        var vendor_nilai55 = it.data_dashboard.total_uang_vendor[4].total_pendapatan

                        val pieChartEntryVendor = ArrayList<Entry>()
                        pieChartEntryVendor.add( Entry(vendor_nilai11.toFloat(), 0 ))
                        pieChartEntryVendor.add( Entry(vendor_nilai22.toFloat(), 1 ))
                        pieChartEntryVendor.add( Entry(vendor_nilai33.toFloat(), 2 ))
                        pieChartEntryVendor.add( Entry(vendor_nilai44.toFloat(), 3 ))
                        pieChartEntryVendor.add( Entry(vendor_nilai55.toFloat(), 4 ))

                        // SETUP PIE CHART ANIMATION
                        pieChartVendor.animateXY(1000, 1000)

                        // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                        val pieDataSetVendor = PieDataSet(pieChartEntryVendor,"")

                        pieDataSetVendor.colors = colors
                        pieDataSetVendor.sliceSpace = 1f
                        pieDataSetVendor.valueTextSize = 8f
                        pieDataSetVendor.valueTextColor = R.color.black

                        // SETUP TEXT IN PIE CHART CENTER
                        pieChartVendor.centerText = "Pendapatan Tiap Vendor"
                        pieChartVendor.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
                        pieChartVendor.setCenterTextSize(10F)

                        // SETUP PIE DATA SET IN PieData
                        val pieDataVendor = PieData(xValuesVendor,pieDataSetVendor)

                        // ENABLED THE VALUE ON EACH PieChartEntry
                        pieDataVendor.setDrawValues(true)
                        pieChartVendor.data = pieDataVendor

                        // OPTIONAL PIE CHART SETUP
                        pieChartVendor.holeRadius = 40f
                        pieChartVendor.transparentCircleRadius = 60f
                        pieChartVendor.setDescription("")
                        pieChartVendor.setExtraOffsets(5f, 10f, 5f, 5f)

                        //SET INFORMASI VENDOR KIOS DELIV --------------------------------------------------------------------------------
                        var saldo_lunasinn = it.data_dashboard.transaksi_vendor[4].balance
                        var saldo_ipnn = it.data_dashboard.transaksi_vendor[0].balance
                        var saldo_teleanjarr = it.data_dashboard.transaksi_vendor[3].balance
                        var saldo_dflashh = it.data_dashboard.transaksi_vendor[2].balance
                        var saldo_mobilepulsaa = it.data_dashboard.transaksi_vendor[1].balance

                        //FORMAT
                        var saldo_lunasinn_format = "Rp " + currencyformat.format(saldo_lunasinn.toLong()).toString()
                        var saldo_ipnn_format = "Rp " + currencyformat.format(saldo_ipnn.toLong()).toString()
                        var saldo_teleanjarr_format = "Rp " + currencyformat.format(saldo_teleanjarr.toLong()).toString()
                        var saldo_dflashh_format = "Rp " + currencyformat.format(saldo_dflashh.toLong()).toString()
                        var saldo_mobilepulsaa_format = "Rp " + currencyformat.format(saldo_mobilepulsaa.toLong()).toString()

                        var transaksi_lunasin = currencyformat.format(it.data_dashboard.transaksi_vendor[4].total_transaksi)
                        var transaksi_ipn = currencyformat.format(it.data_dashboard.transaksi_vendor[0].total_transaksi)
                        var transaksi_teleanjar = currencyformat.format(it.data_dashboard.transaksi_vendor[3].total_transaksi)
                        var transaksi_dflash = currencyformat.format(it.data_dashboard.transaksi_vendor[2].total_transaksi)
                        var transaksi_mobilepulsa = currencyformat.format(it.data_dashboard.transaksi_vendor[1].total_transaksi)

                        var transaksi_lunasin_bulan_lalu = currencyformat.format(it.data_dashboard.transaksi_vendor_bulan_lalu[4].total_transaksi)
                        var transaksi_ipn_bulan_lalu = currencyformat.format(it.data_dashboard.transaksi_vendor_bulan_lalu[0].total_transaksi)
                        var transaksi_teleanjar_bulan_lalu = currencyformat.format(it.data_dashboard.transaksi_vendor_bulan_lalu[3].total_transaksi)
                        var transaksi_dflash_bulan_lalu = currencyformat.format(it.data_dashboard.transaksi_vendor_bulan_lalu[2].total_transaksi)
                        var transaksi_mobilepulsa_bulan_lalu = currencyformat.format(it.data_dashboard.transaksi_vendor_bulan_lalu[1].total_transaksi)

                        saldo_lunasin?.text = saldo_lunasinn_format
                        saldo_ipn?.text = saldo_ipnn_format
                        saldo_teleanjar?.text = saldo_teleanjarr_format
                        saldo_dflash?.text = saldo_dflashh_format
                        saldo_mobilepulsa?.text = saldo_mobilepulsaa_format

                        jumlah_transaksi_lunasin?.text = transaksi_lunasin.toString()
                        jumlah_transaksi_ipn?.text = transaksi_ipn.toString()
                        jumlah_transaksi_teleanjar?.text = transaksi_teleanjar.toString()
                        jumlah_transaksi_dflash?.text = transaksi_dflash.toString()
                        jumlah_transaksi_mobilepulsa?.text = transaksi_mobilepulsa.toString()

                        jumlah_transaksi_bulan_lalu_lunasin?.text = transaksi_lunasin_bulan_lalu.toString()
                        jumlah_transaksi_bulan_lalu_ipn?.text = transaksi_ipn_bulan_lalu.toString()
                        jumlah_transaksi_bulan_lalu_teleanjar?.text = transaksi_teleanjar_bulan_lalu.toString()
                        jumlah_transaksi_bulan_lalu_dflash?.text = transaksi_dflash_bulan_lalu.toString()
                        jumlah_transaksi_bulan_lalu_mobilepulsa?.text = transaksi_mobilepulsa_bulan_lalu.toString()

                    }
                }
            }

            override fun onFailure(call: Call<ResponseDashboard>, t: Throwable) {
                Log.e("pesan error","${t.message}")

                Toast.makeText(this@DashboardDirekturActivity,
                    "Gagal mendapatkan data",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun loadDashboardFilter(){

        btn_apply_tanggal_direktur?.setOnClickListener {

            val loading = LoadingDialog(this)
            loading.startLoading()

            RetrofitClient.myApiClient().dashboardFilter(startDate, endDate).enqueue(object : Callback<ResponseDashboardFilter>{
                override fun onResponse(
                    call: Call<ResponseDashboardFilter>,
                    response: Response<ResponseDashboardFilter>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {

                            loading.dismissLoading()

                            val currencyformat = DecimalFormat("#,###")

                            Toast.makeText(this@DashboardDirekturActivity,"Berhasil filter data " + startDate + " sampai " + endDate,Toast.LENGTH_LONG).show()

                            //SET TOTAL TRANSAKSI --------------------------------------------------------------------------------
                            var indikator_total_transaksi = it.data_dashboard_filter.indikator  + " %"
                            var total_transaksi = it.data_dashboard_filter.total_transaksi
                            var total_transaksi_bulan_lalu = it.data_dashboard_filter.total_transaksi_bulan_lalu

                            if (total_transaksi > total_transaksi_bulan_lalu){
                                indikator_total_transaksii?.setImageResource(R.drawable.icon_up)
                            } else {
                                indikator_total_transaksii?.setImageResource(R.drawable.icon_down)
                            }

                            var total_transaksi_format = currencyformat.format(total_transaksi.toLong())
                            var total_transaksi_bulan_lalu_format = currencyformat.format((total_transaksi_bulan_lalu.toLong()))

                            total_transaksi_persen?.text = indikator_total_transaksi
                            total_transaksii?.text = total_transaksi_format.toString()
                            total_transaksi_bulan_laluu?.text = total_transaksi_bulan_lalu_format.toString()

                            //SET PENDAPATAN TIAP SERVICE
                            var nama_service1 = it.data_dashboard_filter.total_uang_service[0].name
                            var nama_service2 = it.data_dashboard_filter.total_uang_service[1].name
                            var nama_service3 = it.data_dashboard_filter.total_uang_service[2].name
                            var nama_service4 = it.data_dashboard_filter.total_uang_service[3].name
                            var nama_service5 = it.data_dashboard_filter.total_uang_service[4].name

                            var service_nilai1 = it.data_dashboard_filter.total_uang_service[0].total_pendapatan
                            var service_nilai2 = it.data_dashboard_filter.total_uang_service[1].total_pendapatan
                            var service_nilai3 = it.data_dashboard_filter.total_uang_service[2].total_pendapatan
                            var service_nilai4 = it.data_dashboard_filter.total_uang_service[3].total_pendapatan
                            var service_nilai5 = it.data_dashboard_filter.total_uang_service[4].total_pendapatan

                            var service_nilai1_format = currencyformat.format(service_nilai1.toLong())
                            var service_nilai2_format = currencyformat.format(service_nilai2.toLong())
                            var service_nilai3_format = currencyformat.format(service_nilai3.toLong())
                            var service_nilai4_format = currencyformat.format(service_nilai4.toLong())
                            var service_nilai5_format = currencyformat.format(service_nilai5.toLong())

                            service1?.text = nama_service1
                            service2?.text = nama_service2
                            service3?.text = nama_service3
                            service4?.text = nama_service4
                            service5?.text = nama_service5

                            nilai_service1?.text = "Rp " + service_nilai1_format.toString()
                            nilai_service2?.text = "Rp " + service_nilai2_format.toString()
                            nilai_service3?.text = "Rp " + service_nilai3_format.toString()
                            nilai_service4?.text = "Rp " + service_nilai4_format.toString()
                            nilai_service5?.text = "Rp " + service_nilai5_format.toString()

                            //SET PIE CHART DATA SERVICE --------------------------------------------------------------------------------
                            var nama_service11 = it.data_dashboard_filter.total_uang_service[0].name
                            var nama_service22 = it.data_dashboard_filter.total_uang_service[1].name
                            var nama_service33 = it.data_dashboard_filter.total_uang_service[2].name
                            var nama_service44 = it.data_dashboard_filter.total_uang_service[3].name
                            var nama_service55 = it.data_dashboard_filter.total_uang_service[4].name

                            val xValuesService = ArrayList<String>()
                            xValuesService.add(nama_service11)
                            xValuesService.add(nama_service22)
                            xValuesService.add(nama_service33)
                            xValuesService.add(nama_service44)
                            xValuesService.add(nama_service55)

                            var service_nilai11 = it.data_dashboard_filter.total_uang_service[0].total_pendapatan
                            var service_nilai22 = it.data_dashboard_filter.total_uang_service[1].total_pendapatan
                            var service_nilai33 = it.data_dashboard_filter.total_uang_service[2].total_pendapatan
                            var service_nilai44 = it.data_dashboard_filter.total_uang_service[3].total_pendapatan
                            var service_nilai55 = it.data_dashboard_filter.total_uang_service[4].total_pendapatan

                            val pieChartEntryService = ArrayList<Entry>()
                            pieChartEntryService.add( Entry(service_nilai11.toFloat(), 0 ))
                            pieChartEntryService.add( Entry(service_nilai22.toFloat(), 1 ))
                            pieChartEntryService.add( Entry(service_nilai33.toFloat(), 2 ))
                            pieChartEntryService.add( Entry(service_nilai44.toFloat(), 3 ))
                            pieChartEntryService.add( Entry(service_nilai55.toFloat(), 4 ))

                            // SETUP PIE CHART ANIMATION
                            pieChartService.animateXY(1000, 1000)

                            // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                            val pieDataSetService = PieDataSet(pieChartEntryService,"")
                            val colorsService = arrayListOf<Int>()
                            colorsService.add(ColorTemplate.rgb("#61d4b3"))
                            colorsService.add(ColorTemplate.rgb("#fdd365"))
                            colorsService.add(ColorTemplate.rgb("#f0134d"))
                            colorsService.add(ColorTemplate.rgb("#fd2eb3"))
                            colorsService.add(ColorTemplate.rgb("#ff677d"))
                            colorsService.add(ColorTemplate.rgb("#ff9d9d"))
                            colorsService.add(ColorTemplate.rgb("#633a82"))
                            colorsService.random()

                            pieDataSetService.colors = colorsService
                            pieDataSetService.sliceSpace = 1f
                            pieDataSetService.valueTextSize = 7f
                            pieDataSetService.valueTextColor = R.color.grayMuda

                            // SETUP TEXT IN PIE CHART CENTER
                            pieChartService.centerText = "Pendapatan Tiap Service"
                            pieChartService.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
                            pieChartService.setCenterTextSize(9F)

                            // SETUP PIE DATA SET IN PieData
                            val pieDataService = PieData( xValuesService,pieDataSetService)

                            // ENABLED THE VALUE ON EACH PieChartEntry
                            pieDataService.setDrawValues(true)
                            pieChartService.data = pieDataService

                            // OPTIONAL PIE CHART SETUP
                            pieChartService.holeRadius = 35f
                            pieChartService.setBackgroundColor(resources.getColor(R.color.gray))
                            pieChartService.transparentCircleRadius = 55f
                            pieChartService.setDescription("")
                            pieChartService.setExtraOffsets(5f, 10f, 5f, 5f)

                            //SET LEGEND CUSTOMIZATION
                            val legend: Legend = pieChartService.legend
                            legend.textColor = resources.getColor(R.color.white)
                            legend.position = Legend.LegendPosition.BELOW_CHART_LEFT
                            legend.textSize = 8f

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
                            var nilai_service_sukses_terbanyak = currencyformat.format(it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.total)
                            var nilai_service_sukses_terbanyak_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_service_complete.total_transaksi_service_sukses_terbanyak.bulan_lalu)

                            if (nilai_service_sukses_terbanyak > nilai_service_sukses_terbanyak_bulan_lalu){
                                indikator_transaksi_sukses.setImageResource(R.drawable.icon_up)
                            } else{
                                indikator_transaksi_sukses.setImageResource(R.drawable.icon_down)
                            }

                            nama_transaksi_sukses_terbanyak?.text = service_sukses_terbanyak
                            transaksi_sukses_persen?.text = indikator_service_sukses_terbanyak
                            transaksi_sukses_terbanyak?.text = nilai_service_sukses_terbanyak.toString()
                            transaksi_sukses_bulan_lalu?.text = nilai_service_sukses_terbanyak_bulan_lalu.toString()


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
                            var nilai_service_gagal_terbanyak = currencyformat.format(it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.total)
                            var nilai_service_gagal_terbanyak_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_service_failed.total_transaksi_service_gagal_terbanyak.bulan_lalu)

                            if (nilai_service_gagal_terbanyak > nilai_service_gagal_terbanyak_bulan_lalu){
                                indikator_transaksi_gagal.setImageResource(R.drawable.icon_down)
                            } else {
                                indikator_transaksi_gagal.setImageResource(R.drawable.icon_up)
                            }

                            transaksi_gagal_persen?.text = indikator_service_gagal_terbanyak
                            nama_transaksi_gagal_terbanyak?.text = nama_service_gagal_terbanyak
                            transaksi_gagal_terbanyak?.text = nilai_service_gagal_terbanyak.toString()
                            transaksi_gagal_bulan_lalu?.text = nilai_service_gagal_terbanyak_bulan_lalu.toString()

                            //SET PENDAPATAN KIOS DELIV --------------------------------------------------------------------------------
                            var indikator_pendapatan_kotor = it.data_dashboard_filter.total_pendapatan.indikator_total_pendapatan_kotor
                            var nilai_pendapatan_kotor = it.data_dashboard_filter.total_pendapatan.total_pendapatan_kotor.toInt()
                            var nilai_pendapatan_kotor_bulan_lalu = it.data_dashboard_filter.total_pendapatan.total_pendapatan_kotor_bulan_lalu.toInt()

                            var indikator_komisi = it.data_dashboard_filter.total_pendapatan.indikator_total_komisi
                            var nilai_komisi = it.data_dashboard_filter.total_pendapatan.total_komisi.toInt()
                            var nilai_komisi_bulan_lalu = it.data_dashboard_filter.total_pendapatan.total_komisi_bulan_lalu.toInt()

                            var indikator_pendapatan_bersih = it.data_dashboard_filter.total_pendapatan.indikator_total_pendapatan_bersih
                            var nilai_pendapatan_bersih = it.data_dashboard_filter.total_pendapatan.total_pendapatan_bersih.toInt()
                            var nilai_pendapatan_bersih_bulan_lalu = it.data_dashboard_filter.total_pendapatan.total_pendapatan_bersih_bulan_lalu.toInt()

                            //FORMAT
                            var nilai_pendapatan_kotor_format = "Rp " + currencyformat.format(nilai_pendapatan_kotor.toLong()).toString()
                            var nilai_pendapatan_kotor_bulan_lalu_format = "Rp " + currencyformat.format(nilai_pendapatan_kotor_bulan_lalu.toLong()).toString()

                            if (nilai_pendapatan_kotor > nilai_pendapatan_kotor_bulan_lalu) {
                                indikator_total_pendapatan_kotor.setImageResource(R.drawable.icon_up)
                            } else {
                                indikator_total_pendapatan_kotor.setImageResource(R.drawable.icon_down)
                            }

                            var nilai_komisi_format = "Rp " + currencyformat.format(nilai_komisi.toLong()).toString()
                            var nilai_komisi_bulan_lalu_format = "Rp " + currencyformat.format(nilai_komisi_bulan_lalu.toLong()).toString()

                            if (nilai_komisi > nilai_komisi_bulan_lalu) {
                                indikator_total_komisi.setImageResource(R.drawable.icon_up)
                            } else {
                                indikator_total_komisi.setImageResource(R.drawable.icon_down)
                            }

                            var nilai_pendapatan_bersih_format = "Rp " + currencyformat.format(nilai_pendapatan_bersih.toLong()).toString()
                            var nilai_pendapataan_bersih_bulan_lalu_format = "Rp " + currencyformat.format(nilai_pendapatan_bersih_bulan_lalu.toLong()).toString()

                            if (nilai_pendapatan_bersih > nilai_pendapatan_bersih_bulan_lalu) {
                                indikator_total_pendapatan_bersih.setImageResource(R.drawable.icon_up)
                            } else {
                                indikator_total_pendapatan_bersih.setImageResource(R.drawable.icon_down)
                            }

                            total_pendapatan_kotor_persen?.text = indikator_pendapatan_kotor + " %"
                            total_pendapatan_kotor?.text = nilai_pendapatan_kotor_format
                            total_pendapatan_kotor_bulan_lalu?.text = nilai_pendapatan_kotor_bulan_lalu_format

                            total_komisi_persen?.text = indikator_komisi + " %"
                            total_komisi?.text = nilai_komisi_format
                            total_komisi_bulan_lalu?.text = nilai_komisi_bulan_lalu_format

                            total_pendapatan_bersih_persen?.text = indikator_pendapatan_bersih + " %"
                            total_pendapatan_bersih?.text = nilai_pendapatan_bersih_format
                            total_pendapatan_bersih_bulan_lalu?.text = nilai_pendapataan_bersih_bulan_lalu_format

                            //SET PENDAPATAN TIAP VENDOR
                            var nama_vendor1 = it.data_dashboard_filter.total_uang_vendor[0].name
                            var nama_vendor2 = it.data_dashboard_filter.total_uang_vendor[1].name
                            var nama_vendor3 = it.data_dashboard_filter.total_uang_vendor[2].name
                            var nama_vendor4 = it.data_dashboard_filter.total_uang_vendor[3].name
                            var nama_vendor5 = it.data_dashboard_filter.total_uang_vendor[4].name

                            var vendor_nilai1 = it.data_dashboard_filter.total_uang_vendor[0].total_pendapatan
                            var vendor_nilai2 = it.data_dashboard_filter.total_uang_vendor[1].total_pendapatan
                            var vendor_nilai3 = it.data_dashboard_filter.total_uang_vendor[2].total_pendapatan
                            var vendor_nilai4 = it.data_dashboard_filter.total_uang_vendor[3].total_pendapatan
                            var vendor_nilai5 = it.data_dashboard_filter.total_uang_vendor[4].total_pendapatan

                            var vendor_nilai1_format = currencyformat.format(vendor_nilai1.toLong())
                            var vendor_nilai2_format = currencyformat.format(vendor_nilai2.toLong())
                            var vendor_nilai3_format = currencyformat.format(vendor_nilai3.toLong())
                            var vendor_nilai4_format = currencyformat.format(vendor_nilai4.toLong())
                            var vendor_nilai5_format = currencyformat.format(vendor_nilai5.toLong())

                            vendor1?.text = nama_vendor1
                            vendor2?.text = nama_vendor2
                            vendor3?.text = nama_vendor3
                            vendor4?.text = nama_vendor4
                            vendor5?.text = nama_vendor5

                            nilai_vendor1?.text = "Rp " + vendor_nilai1_format.toString()
                            nilai_vendor2?.text = "Rp " + vendor_nilai2_format.toString()
                            nilai_vendor3?.text = "Rp " + vendor_nilai3_format.toString()
                            nilai_vendor4?.text = "Rp " + vendor_nilai4_format.toString()
                            nilai_vendor5?.text = "Rp " + vendor_nilai5_format.toString()

                            //SET PIE CHART DATA VENDOR --------------------------------------------------------------------------------
                            var nama_vendor11 = it.data_dashboard_filter.total_uang_vendor[0].name
                            var nama_vendor22 = it.data_dashboard_filter.total_uang_vendor[1].name
                            var nama_vendor33 = it.data_dashboard_filter.total_uang_vendor[2].name
                            var nama_vendor44 = it.data_dashboard_filter.total_uang_vendor[3].name
                            var nama_vendor55 = it.data_dashboard_filter.total_uang_vendor[4].name

                            val xValuesVendor = ArrayList<String>()
                            xValuesVendor.add(nama_vendor11)
                            xValuesVendor.add(nama_vendor22)
                            xValuesVendor.add(nama_vendor33)
                            xValuesVendor.add(nama_vendor44)
                            xValuesVendor.add(nama_vendor55)

                            var vendor_nilai11 = it.data_dashboard_filter.total_uang_vendor[0].total_pendapatan
                            var vendor_nilai22 = it.data_dashboard_filter.total_uang_vendor[1].total_pendapatan
                            var vendor_nilai33 = it.data_dashboard_filter.total_uang_vendor[2].total_pendapatan
                            var vendor_nilai44 = it.data_dashboard_filter.total_uang_vendor[3].total_pendapatan
                            var vendor_nilai55 = it.data_dashboard_filter.total_uang_vendor[4].total_pendapatan

                            val pieChartEntryVendor = ArrayList<Entry>()
                            pieChartEntryVendor.add( Entry(vendor_nilai11.toFloat(), 0 ))
                            pieChartEntryVendor.add( Entry(vendor_nilai22.toFloat(), 1 ))
                            pieChartEntryVendor.add( Entry(vendor_nilai33.toFloat(), 2 ))
                            pieChartEntryVendor.add( Entry(vendor_nilai44.toFloat(), 3 ))
                            pieChartEntryVendor.add( Entry(vendor_nilai55.toFloat(), 4 ))

                            // SETUP PIE CHART ANIMATION
                            pieChartVendor.animateXY(1000, 1000)

                            // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
                            val pieDataSetVendor = PieDataSet(pieChartEntryVendor,"")

                            pieDataSetVendor.colors = colors
                            pieDataSetVendor.sliceSpace = 1f
                            pieDataSetVendor.valueTextSize = 8f
                            pieDataSetVendor.valueTextColor = R.color.black

                            // SETUP TEXT IN PIE CHART CENTER
                            pieChartVendor.centerText = "Pendapatan Tiap Vendor"
                            pieChartVendor.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
                            pieChartVendor.setCenterTextSize(10F)

                            // SETUP PIE DATA SET IN PieData
                            val pieDataVendor = PieData(xValuesVendor,pieDataSetVendor)

                            // ENABLED THE VALUE ON EACH PieChartEntry
                            pieDataVendor.setDrawValues(true)
                            pieChartVendor.data = pieDataVendor

                            // OPTIONAL PIE CHART SETUP
                            pieChartVendor.holeRadius = 40f
                            pieChartVendor.transparentCircleRadius = 60f
                            pieChartVendor.setDescription("")
                            pieChartVendor.setExtraOffsets(5f, 10f, 5f, 5f)

                            //SET INFORMASI VENDOR KIOS DELIV --------------------------------------------------------------------------------
                            var saldo_lunasinn = it.data_dashboard_filter.transaksi_vendor[4].balance
                            var saldo_ipnn = it.data_dashboard_filter.transaksi_vendor[0].balance
                            var saldo_teleanjarr = it.data_dashboard_filter.transaksi_vendor[3].balance
                            var saldo_dflashh = it.data_dashboard_filter.transaksi_vendor[2].balance
                            var saldo_mobilepulsaa = it.data_dashboard_filter.transaksi_vendor[1].balance

                            //FORMAT
                            var saldo_lunasinn_format = "Rp " + currencyformat.format(saldo_lunasinn.toLong()).toString()
                            var saldo_ipnn_format = "Rp " + currencyformat.format(saldo_ipnn.toLong()).toString()
                            var saldo_teleanjarr_format = "Rp " + currencyformat.format(saldo_teleanjarr.toLong()).toString()
                            var saldo_dflashh_format = "Rp " + currencyformat.format(saldo_dflashh.toLong()).toString()
                            var saldo_mobilepulsaa_format = "Rp " + currencyformat.format(saldo_mobilepulsaa.toLong()).toString()

                            var transaksi_lunasin = currencyformat.format(it.data_dashboard_filter.transaksi_vendor[4].total_transaksi)
                            var transaksi_ipn = currencyformat.format(it.data_dashboard_filter.transaksi_vendor[0].total_transaksi)
                            var transaksi_teleanjar = currencyformat.format(it.data_dashboard_filter.transaksi_vendor[3].total_transaksi)
                            var transaksi_dflash = currencyformat.format(it.data_dashboard_filter.transaksi_vendor[2].total_transaksi)
                            var transaksi_mobilepulsa = currencyformat.format(it.data_dashboard_filter.transaksi_vendor[1].total_transaksi)

                            var transaksi_lunasin_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_vendor_bulan_lalu[4].total_transaksi)
                            var transaksi_ipn_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_vendor_bulan_lalu[0].total_transaksi)
                            var transaksi_teleanjar_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_vendor_bulan_lalu[3].total_transaksi)
                            var transaksi_dflash_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_vendor_bulan_lalu[2].total_transaksi)
                            var transaksi_mobilepulsa_bulan_lalu = currencyformat.format(it.data_dashboard_filter.transaksi_vendor_bulan_lalu[1].total_transaksi)

                            saldo_lunasin?.text = saldo_lunasinn_format.toString()
                            saldo_ipn?.text = saldo_ipnn_format.toString()
                            saldo_teleanjar?.text = saldo_teleanjarr_format
                            saldo_dflash?.text = saldo_dflashh_format
                            saldo_mobilepulsa?.text = saldo_mobilepulsaa_format

                            jumlah_transaksi_lunasin?.text = transaksi_lunasin.toString()
                            jumlah_transaksi_ipn?.text = transaksi_ipn.toString()
                            jumlah_transaksi_teleanjar?.text = transaksi_teleanjar.toString()
                            jumlah_transaksi_dflash?.text = transaksi_dflash.toString()
                            jumlah_transaksi_mobilepulsa?.text = transaksi_mobilepulsa.toString()

                            jumlah_transaksi_bulan_lalu_lunasin?.text = transaksi_lunasin_bulan_lalu.toString()
                            jumlah_transaksi_bulan_lalu_ipn?.text = transaksi_ipn_bulan_lalu.toString()
                            jumlah_transaksi_bulan_lalu_teleanjar?.text = transaksi_teleanjar_bulan_lalu.toString()
                            jumlah_transaksi_bulan_lalu_dflash?.text = transaksi_dflash_bulan_lalu.toString()
                            jumlah_transaksi_bulan_lalu_mobilepulsa?.text = transaksi_mobilepulsa_bulan_lalu.toString()

                        }
                    } else {
                        Toast.makeText(this@DashboardDirekturActivity,
                            "Data filter tidak sesuai",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseDashboardFilter>, t: Throwable) {
                    Log.e("pesan error","${t.message}")

                    Toast.makeText(this@DashboardDirekturActivity,
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
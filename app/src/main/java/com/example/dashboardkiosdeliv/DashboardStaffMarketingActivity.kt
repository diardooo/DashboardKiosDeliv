package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class DashboardStaffMarketingActivity : AppCompatActivity() {

    private lateinit var pieChart1: PieChart
    private lateinit var pieChart2: PieChart
    private lateinit var tvDatePickerFrom: TextView
    private lateinit var btnDatePickerFrom: CardView
    private lateinit var tvDatePickerTo: TextView
    private lateinit var btnDatePickerTo: CardView

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

        setPiechart1()
        setPieChart2()

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

    }

    private fun setPiechart1() {

        // SETUP PIE CHAR ENTRIES
        val xValues = ArrayList<String>()
        xValues.add("PLN Pascabayar")
        xValues.add("PLN Prabayar")
        xValues.add("Lainnya")

        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(87.12F, 0))
        pieChartEntry.add(Entry(6.76f, 1))
        pieChartEntry.add(Entry(5.45f, 2))

        // SETUP PIE CHART ANIMATION
        pieChart1.animateXY(1000, 1000)

        // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
        val pieDataSet = PieDataSet(pieChartEntry, "")
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
        pieDataSet.sliceSpace = 3f
        pieDataSet.valueTextSize = 10f
        pieDataSet.valueTextColor = R.color.black

        // SETUP TEXT IN PIE CHART CENTER
        pieChart1.centerText = "Transaksi Berhasil"
        pieChart1.setCenterTextColor(resources.getColor(android.R.color.holo_green_dark))
        pieChart1.setCenterTextSize(10F)

        // SETUP PIE DATA SET IN PieData
        val pieData = PieData(xValues, pieDataSet)

        // ENABLED THE VALUE ON EACH PieChartEntry
        pieData.setDrawValues(true)
        pieChart1.data = pieData

        // OPTIONAL PIE CHART SETUP
        pieChart1.holeRadius = 40f
        pieChart1.setBackgroundColor(resources.getColor(R.color.white))
        pieChart1.transparentCircleRadius = 60f
        pieChart1.setDescription("")
        pieChart1.setExtraOffsets(5f, 10f, 5f, 5f)

    }

    private fun setPieChart2() {

        // SETUP PIE CHAR ENTRIES
        val xValues = ArrayList<String>()
        xValues.add("PLN Pascabayar")
        xValues.add("PLN Prabayar")

        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(71f, 0))
        pieChartEntry.add(Entry(28f, 1))

        // SETUP PIE CHART ANIMATION
        pieChart2.animateXY(1000, 1000)

        // SETUP PIECHART ENTRIES COLORS, CHART SLICE SPACE, AND TEXT
        val pieDataSet = PieDataSet(pieChartEntry, "")
        val colors = arrayListOf<Int>()
        colors.add(ColorTemplate.rgb("#f0134d"))
        colors.add(ColorTemplate.rgb("#fb8d62"))
        colors.add(ColorTemplate.rgb("#fd2eb3"))
        colors.add(ColorTemplate.rgb("#ff677d"))
        colors.add(ColorTemplate.rgb("#ff9d9d"))
        colors.add(ColorTemplate.rgb("#633a82"))
        colors.add(ColorTemplate.rgb("#61d4b3"))
        colors.add(ColorTemplate.rgb("#fdd365"))
        colors.random()

        pieDataSet.colors = colors
        pieDataSet.sliceSpace = 3f
        pieDataSet.valueTextSize = 12f
        pieDataSet.valueTextColor = R.color.black

        // SETUP TEXT IN PIE CHART CENTER
        pieChart2.centerText = "Transaksi Gagal"
        pieChart2.setCenterTextColor(resources.getColor(android.R.color.holo_red_dark))
        pieChart2.setCenterTextSize(10F)

        // SETUP PIE DATA SET IN PieData
        val pieData = PieData(xValues, pieDataSet)

//        pieData.setValueFormatter(PercentFormatter())
//        pieChart2.setUsePercentValues(true)

        // ENABLED THE VALUE ON EACH PieChartEntry
        pieData.setDrawValues(true)
        pieChart2.data = pieData

        // OPTIONAL PIE CHART SETUP
        pieChart2.holeRadius = 40f
        pieChart2.setBackgroundColor(resources.getColor(R.color.white))
        pieChart2.transparentCircleRadius = 60f
        pieChart2.setDescription("")
        pieChart2.setExtraOffsets(5f, 10f, 5f, 5f)

    }

    private fun dateFrom(myCalendar: Calendar) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerFrom.setText(sdf.format(myCalendar.time))

    }

    private fun dateTo(myCalendar: Calendar) {

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerTo.setText(sdf.format(myCalendar.time))

    }

//    private fun initChart(chart : PieChart){
//
//        chart.setUsePercentValues(true)
//        chart.description.isEnabled = false
//        chart.setExtraOffsets(5f, 10f, 5f, 5f)
//
//        chart.dragDecelerationFrictionCoef = 0.95f
//
//        chart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
//        chart.setCenterTextSize(15f)
//
//        chart.isDrawHoleEnabled = true
//        chart.setHoleColor(Color.WHITE)
//
//        chart.setTransparentCircleColor(Color.WHITE)
//        chart.setTransparentCircleAlpha(110)
//
//        chart.holeRadius = 58f
//        chart.transparentCircleRadius = 61f
//
//        chart.setDrawCenterText(true)
//
//        chart.rotationAngle = 0f
//        chart.isRotationEnabled = true
//        chart.isHighlightPerTapEnabled = true
//
//        chart.animateY(1400, Easing.EaseInOutQuad)
//
//        val l = chart.legend
//        l.isEnabled = false
//        chart.setEntryLabelColor(Color.BLACK)
//        chart.setEntryLabelTextSize(12f)
//
//    }
//
//    private fun setChartData(dataSet: PieDataSet, chart: PieChart) {
//
//        dataSet.setDrawIcons(false)
//
//        dataSet.sliceSpace = 3f
//        dataSet.iconsOffset = MPPointF(0f, 40f)
//        dataSet.selectionShift = 5f
//
//        // add a lot of colors
//
//        // add a lot of colors
//        val colors = arrayListOf<Int>()
//
//        colors.add(ColorTemplate.rgb("#61d4b3"))
//        colors.add(ColorTemplate.rgb("#fdd365"))
//        colors.add(ColorTemplate.rgb("#f0134d"))
//        colors.add(ColorTemplate.rgb("#fb8d62"))
//        colors.add(ColorTemplate.rgb("#fd2eb3"))
//        colors.add(ColorTemplate.rgb("#ff677d"))
//        colors.add(ColorTemplate.rgb("#ff9d9d"))
//        colors.add(ColorTemplate.rgb("#633a82"))
//        colors.random()
//
//        dataSet.colors = colors
//
//        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter(chart))
//        data.setValueTextSize(12f)
//        data.setValueTextColor(Color.BLACK)
//        chart.data = data
//
//        // undo all highlights
//        chart.highlightValues(null)
//
//        chart.invalidate()
//
//    }
}
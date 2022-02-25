package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgressLoketActivity : AppCompatActivity() {

    private lateinit var tvDatePickerFrom : TextView
    private lateinit var btnDatePickerFrom : CardView
    private lateinit var tvDatePickerTo : TextView
    private lateinit var btnDatePickerTo : CardView
    private lateinit var barChart : BarChart

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

        setBarChartData()
    }
    private fun setBarChartData(){

        // X Axis values
        val xValues = ArrayList<String>()
        xValues.add("April, 2021")
        xValues.add("May, 2021")
        xValues.add("Jun, 2021")
        xValues.add("Jul, 2021")
        xValues.add("Aug, 2021")
        xValues.add("Sep, 2021")
        xValues.add("Oct, 2021")
        xValues.add("Nov, 2021")
        xValues.add("Dec, 2021")
        xValues.add("Jan, 2022")
        xValues.add("Feb, 2022")

        // y axis values or bar data
        val barEntry = ArrayList<BarEntry>()
        barEntry.add(BarEntry(107F, 0))
        barEntry.add(BarEntry(86F, 1))
        barEntry.add(BarEntry(80F, 2))
        barEntry.add(BarEntry(55F, 3))
        barEntry.add(BarEntry(40F, 4))
        barEntry.add(BarEntry(33F, 5))
        barEntry.add(BarEntry(34F, 6))
        barEntry.add(BarEntry(28F, 7))
        barEntry.add(BarEntry(24F, 8))
        barEntry.add(BarEntry(161F, 9))
        barEntry.add(BarEntry(71F, 10))

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

        val data = BarData(xValues, barDataSet)

        barChart.data = data
        barChart.setBackgroundColor(resources.getColor(R.color.white))
        barChart.animateXY(1100,1100)
        barChart.setDescription("")

    }

    private fun dateFrom(myCalendar: Calendar){

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerFrom.setText(sdf.format(myCalendar.time))
    }

    private fun dateTo(myCalendar: Calendar){

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
        tvDatePickerTo.setText(sdf.format(myCalendar.time))
    }

}
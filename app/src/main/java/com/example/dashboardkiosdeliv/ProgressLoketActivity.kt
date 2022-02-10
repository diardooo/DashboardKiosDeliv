package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgressLoketActivity : AppCompatActivity() {

    private lateinit var tvDatePickerFrom : TextView
    private lateinit var btnDatePickerFrom : CardView
    private lateinit var tvDatePickerTo : TextView
    private lateinit var btnDatePickerTo : CardView
    private lateinit var lineChart : LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_loket)

        //INISIALISASI
        var btnBack = findViewById<ImageView>(R.id.img_back_progress_loket)

        tvDatePickerFrom = findViewById(R.id.tv_date_from_loket)
        btnDatePickerFrom = findViewById(R.id.btn_date_form_loket)
        tvDatePickerTo = findViewById(R.id.tv_date_to_loket)
        btnDatePickerTo = findViewById(R.id.btn_date_to_loket)
        lineChart = findViewById(R.id.chart_progres_loket)

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

        setLineChartData()
    }
    private fun setLineChartData(){
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

        val lineEntry = ArrayList<Entry>()
        lineEntry.add(Entry(107F, 0))
        lineEntry.add(Entry(86F, 1))
        lineEntry.add(Entry(80F, 2))
        lineEntry.add(Entry(55F, 3))
        lineEntry.add(Entry(40F, 4))
        lineEntry.add(Entry(33F, 5))
        lineEntry.add(Entry(34F, 6))
        lineEntry.add(Entry(28F, 7))
        lineEntry.add(Entry(24F, 8))
        lineEntry.add(Entry(161F, 9))
        lineEntry.add(Entry(71F, 10))

        val lineDataSet = LineDataSet(lineEntry, "Line Chart")
        lineDataSet.color = resources.getColor(R.color.grayMuda)
        lineDataSet.circleRadius = 0F
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillAlpha = 30
        lineDataSet.setDrawCubic(true)
        lineDataSet.valueTextSize = 10f
        lineDataSet.valueTextColor = R.color.black

        val data = LineData(xValues, lineDataSet)

        lineChart.data = data
        lineChart.setBackgroundColor(resources.getColor(R.color.white))
        lineChart.animateXY(3000,300)
        lineChart.setDescription("")

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
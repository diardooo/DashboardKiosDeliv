package com.example.dashboardkiosdeliv

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.dashboardkiosdeliv.model.ResponseDeposit
import com.example.dashboardkiosdeliv.model.ResponseDepositFilter
import com.example.dashboardkiosdeliv.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_informasi_deposit.*
import kotlinx.android.synthetic.main.activity_progress_loket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class InformasiDepositActivity : AppCompatActivity() {

    private lateinit var btnBack : ImageView
    private lateinit var tvDatePickerFrom : TextView
    private lateinit var btnDatePickerFrom : CardView
    private lateinit var tvDatePickerTo : TextView
    private lateinit var btnDatePickerTo : CardView
    private lateinit var btnApplyDate : Button
    private lateinit var startDate : String
    private lateinit var endDate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_deposit)

        //INISIALISASI
        btnBack = findViewById(R.id.img_back_info_deposit)
        tvDatePickerFrom = findViewById(R.id.tv_date_from_depo)
        btnDatePickerFrom = findViewById(R.id.btn_date_form_depo)
        tvDatePickerTo = findViewById(R.id.tv_date_to_depo)
        btnDatePickerTo = findViewById(R.id.btn_date_to_depo)
        btnApplyDate = findViewById(R.id.btn_apply_tanggal_deposit)

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

//        tv_date_from_depo?.text = (sdf.format(Date()))
//        tv_date_to_depo?.text = (sdf.format(Date()))

        loadInfoDeposit()
        loadInfoDepositFilter()

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

    private fun loadInfoDepositFilter(){
        btnApplyDate.setOnClickListener {

            val loading = LoadingDialog(this)
            loading.startLoading()

            RetrofitClient.myApiClient().infoDepositFilter(startDate, endDate).enqueue(object : Callback<ResponseDepositFilter>{
                override fun onResponse(
                    call: Call<ResponseDepositFilter>,
                    response: Response<ResponseDepositFilter>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {

                            loading.dismissLoading()

                            val currencyformat = DecimalFormat("#,###")

                            Toast.makeText(this@InformasiDepositActivity,"Berhasil filter data " + startDate + " sampai " + endDate,Toast.LENGTH_LONG).show()

                            //JUMLAH DEPOSIT TERBANYAK
                            var deposit_total = it.data_deposit_filter.deposit_terbanyak.total_deposit
                            var bank_name = it.data_deposit_filter.deposit_terbanyak.name
                            var jumlah = it.data_deposit_filter.deposit_terbanyak.total
                            var jumlah_bulan_lalu = it.data_deposit_filter.deposit_terbanyak.bulan_lalu

                            //FORMAT CURRENCY
                            var deposit_total_format = "Rp " + currencyformat.format(deposit_total.toLong()).toString()
                            var jumlah_format = "Rp " + currencyformat.format(jumlah.toLong()).toString()
                            var jumlah_bulan_lalu_format = "Rp " + currencyformat.format(jumlah_bulan_lalu.toLong()).toString()

                            deposit_totall?.text = deposit_total_format
                            jumlah_loket_bertransaksi?.text = bank_name
                            total_deposit_bank_terbanyak?.text = jumlah_format
                            total_deposit_bank_terbanyak_bulan_lalu?.text = jumlah_bulan_lalu_format


                            //JUMLAH DEPOSIT TIAP BANK
                            var deposit_bri = it.data_deposit_filter.deposit[0].total
                            var deposit_bca = it.data_deposit_filter.deposit[1].total
                            var deposit_bni = it.data_deposit_filter.deposit[2].total
                            var deposit_mandiri = it.data_deposit_filter.deposit[3].total
                            var deposit_virtual_bni = it.data_deposit_filter.deposit[4].total

                            //FORMAT CURRENCY
                            var deposit_bri_format = "Rp " + currencyformat.format(deposit_bri.toLong()).toString()
                            var deposit_bca_format = "Rp " + currencyformat.format(deposit_bca.toLong()).toString()
                            var deposit_bni_format = "Rp " + currencyformat.format(deposit_bni.toLong()).toString()
                            var deposit_mandiri_format = "Rp " + currencyformat.format(deposit_mandiri.toLong()).toString()
                            var deposit_virtual_bni_format = "Rp " + currencyformat.format(deposit_virtual_bni.toLong()).toString()

                            deposit_virtual_bnii?.text = deposit_virtual_bni_format
                            deposit_mandirii?.text = deposit_mandiri_format
                            deposit_bcaa?.text = deposit_bca_format
                            deposit_brii?.text = deposit_bri_format
                            deposit_bnii?.text = deposit_bni_format

                        }
                    } else {
                        Toast.makeText(this@InformasiDepositActivity,
                            "Data filter tidak sesuai",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(call: Call<ResponseDepositFilter>, t: Throwable) {
                    Log.e("pesan error","${t.message}")

                    Toast.makeText(this@InformasiDepositActivity,
                        "Gagal mendapatkan data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun loadInfoDeposit(){

        val loading = LoadingDialog(this)
        loading.startLoading()

        RetrofitClient.myApiClient().infoDeposit().enqueue(object : Callback<ResponseDeposit>{
            override fun onResponse(
                call: Call<ResponseDeposit>,
                response: Response<ResponseDeposit>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {

                        loading.dismissLoading()

                        val currencyformat = DecimalFormat("#,###")

                        tv_date_from_depo?.text =  "01 " + it.tanggal_info_deposit
                        tv_date_to_depo?.text = "30 " + it.tanggal_info_deposit

                        Toast.makeText(this@InformasiDepositActivity,"Menampilkan otomatis bulan " + it.tanggal_info_deposit,Toast.LENGTH_LONG).show()

                        //JUMLAH DEPOSIT TERBANYAK
                        var deposit_total = it.data_deposit.deposit_terbanyak.total_deposit
                        var bank_name = it.data_deposit.deposit_terbanyak.name
                        var jumlah = it.data_deposit.deposit_terbanyak.total
                        var jumlah_bulan_lalu = it.data_deposit.deposit_terbanyak.bulan_lalu

                        //FORMAT CURRENCY
                        var deposit_total_format = "Rp " + currencyformat.format(deposit_total.toLong()).toString()
                        var jumlah_format = "Rp " + currencyformat.format(jumlah.toLong()).toString()
                        var jumlah_bulan_lalu_format = "Rp " + currencyformat.format(jumlah_bulan_lalu.toLong()).toString()

                        deposit_totall?.text = deposit_total_format
                        jumlah_loket_bertransaksi?.text = bank_name
                        total_deposit_bank_terbanyak?.text = jumlah_format
                        total_deposit_bank_terbanyak_bulan_lalu?.text = jumlah_bulan_lalu_format


                        //JUMLAH DEPOSIT TIAP BANK
                        var deposit_bri = it.data_deposit.deposit[0].total
                        var deposit_bca = it.data_deposit.deposit[1].total
                        var deposit_bni = it.data_deposit.deposit[2].total
                        var deposit_mandiri = it.data_deposit.deposit[3].total
                        var deposit_virtual_bni = it.data_deposit.deposit[4].total

                        //FORMAT CURRENCY
                        var deposit_bri_format = "Rp " + currencyformat.format(deposit_bri.toLong()).toString()
                        var deposit_bca_format = "Rp " + currencyformat.format(deposit_bca.toLong()).toString()
                        var deposit_bni_format = "Rp " + currencyformat.format(deposit_bni.toLong()).toString()
                        var deposit_mandiri_format = "Rp " + currencyformat.format(deposit_mandiri.toLong()).toString()
                        var deposit_virtual_bni_format = "Rp " + currencyformat.format(deposit_virtual_bni.toLong()).toString()

                        deposit_virtual_bnii?.text = deposit_virtual_bni_format
                        deposit_mandirii?.text = deposit_mandiri_format
                        deposit_bcaa?.text = deposit_bca_format
                        deposit_brii?.text = deposit_bri_format
                        deposit_bnii?.text = deposit_bni_format

                    }
                }
            }

            override fun onFailure(call: Call<ResponseDeposit>, t: Throwable) {
                Log.e("pesan error","${t.message}")

                Toast.makeText(this@InformasiDepositActivity,
                    "Gagal mendapatkan data",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}

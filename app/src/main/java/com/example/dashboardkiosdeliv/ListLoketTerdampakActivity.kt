package com.example.dashboardkiosdeliv

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboardkiosdeliv.adapter.ListLoketTerdampakAdapter
import com.example.dashboardkiosdeliv.model.ListLoketTerdampakModel
import java.net.URLEncoder

class ListLoketTerdampakActivity : AppCompatActivity() {

    private lateinit var rvListLoket : RecyclerView
    private lateinit var rvArray : ArrayList<ListLoketTerdampakModel>
    lateinit var foto : Array<Int>
    lateinit var namaLoket : Array<String>
    lateinit var nomorLoket : Array<String>
    lateinit var serviceError : Array<String>
    lateinit var tanggalError : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_loket_terdampak)

        var btnBack = findViewById<ImageView>(R.id.img_back_list_loket_terdampak)

        foto = arrayOf(
            R.drawable.icon_chat,
            R.drawable.icon_chat,
            R.drawable.icon_chat,
            R.drawable.icon_chat,
            R.drawable.icon_chat
        )

        namaLoket = arrayOf(
            "Loket Barokah Abadi",
            "Loket Putra Jaya",
            "Loket Sukorejo",
            "Loket Naufal Cell",
            "Loket Sengkaling"
        )

        nomorLoket = arrayOf(
            "082134714686",
            "082123722311",
            "082156772823",
            "086743473456",
            "083452133473",
        )
        serviceError = arrayOf(
            "PLN Prabayar",
            "PDAM",
            "PLN Pasca Bayar",
            "Pulsa dan Paket data",
            "BPJS"
        )

        tanggalError = arrayOf(
            "25-02-2022, 12.45",
            "24-02-2022, 18.30",
            "22-02-2022, 07.34",
            "20-01-2022, 10.50",
            "19-01-2022, 16.22"
        )

        //EVENT
        btnBack.setOnClickListener {
            intent = Intent(this, DashboardStaffMarketingActivity::class.java)
            startActivity(intent)
            finish()
        }

        rvListLoket = findViewById(R.id.rv_loket_terdampak_error)
        rvListLoket.layoutManager = LinearLayoutManager(this)
        rvListLoket.setHasFixedSize(true)

        rvArray = arrayListOf<ListLoketTerdampakModel>()
        getUserData()
    }

    private fun getUserData() {

        for (i in foto.indices){
            val listLoket = ListLoketTerdampakModel(foto[i], namaLoket[i], nomorLoket[i], serviceError[i], tanggalError[i])
            rvArray.add(listLoket)
        }

        var adapter = ListLoketTerdampakAdapter(rvArray)
        rvListLoket.adapter = adapter

        adapter.setOnItemclickListener(object : ListLoketTerdampakAdapter.onItemClickListener{

            override fun onItemClick(position: Int) {

                val packageManager : PackageManager = packageManager
                val i = Intent(Intent.ACTION_VIEW)
                val url = "https://api.whatsapp.com/send?phone=" + "082134714686" + "&text=" + URLEncoder.encode("Halo Mitra Kios Deliv", "UTF-8")

                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)

                if(i.resolveActivity(packageManager) != null){
                    startActivity(i)
                } else

                Toast.makeText(this@ListLoketTerdampakActivity, "You Clicked on item no. $position", Toast.LENGTH_SHORT).show()

            }

        })

    }
}
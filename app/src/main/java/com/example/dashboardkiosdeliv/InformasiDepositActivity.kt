package com.example.dashboardkiosdeliv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class InformasiDepositActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_deposit)

        var btnBack = findViewById<ImageView>(R.id.img_back_info_deposit)

        btnBack.setOnClickListener {
            intent = Intent(this, MenuDirekturActivity::class.java)
            startActivity(intent)
        }

    }
}
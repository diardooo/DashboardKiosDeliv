package com.example.dashboardkiosdeliv

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

//        setUpToolbar("Ganti Password")
        back()

        var passLama = findViewById<TextView>(R.id.text_password_lama)
        var passBaru = findViewById<TextView>(R.id.text_password_baru)
        var konfirmasiPass = findViewById<TextView>(R.id.text_konfirmasi_password)
        var btnGantiPass = findViewById<Button>(R.id.btn_ganti_password)

        btnGantiPass.setOnClickListener {
            if (passLama.text.toString().isEmpty()){
                passLama.error = "Mohon masukkan password lama terlebih dahulu"
                passLama.requestFocus()
                return@setOnClickListener
            }
            if (passBaru.text.toString().isEmpty()){
                passBaru.error = "Mohon masukkan password baru terlebih dahulu"
                passBaru.requestFocus()
                return@setOnClickListener
            }
            if (konfirmasiPass.text.toString().isEmpty()){
                konfirmasiPass.error = "Mohon masukkan konfirmasi password terlebih dahulu"
                konfirmasiPass.requestFocus()
                return@setOnClickListener
            }
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun back(){
        var back = findViewById<ImageView>(R.id.img_back_chng_pass)
        back.setOnClickListener {
            intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
//    private fun setUpToolbar(title: String){
//        var toolbar = findViewById<Toolbar>(R.id.toolbar_chng_pass)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = title
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return super.onSupportNavigateUp()
//    }
}
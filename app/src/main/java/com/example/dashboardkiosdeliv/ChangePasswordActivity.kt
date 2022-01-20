package com.example.dashboardkiosdeliv

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

//        setUpToolbar("Ganti Password")
        back()
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
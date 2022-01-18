package com.example.dashboardkiosdeliv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.et_username_login)
        val password = findViewById<EditText>(R.id.et_password_login)
        val login = findViewById<Button>(R.id.btn_login)

        login.setOnClickListener {
            if (username.text.toString().isEmpty()){
                username.error = "Mohon masukkan email terlebih dahulu"
                username.requestFocus()
                return@setOnClickListener
            }
            if (password.text.toString().isEmpty()){
                password.error = "Mohon masukkan password terlebih dahulu"
                password.requestFocus()
                return@setOnClickListener
            }
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }
    }


}
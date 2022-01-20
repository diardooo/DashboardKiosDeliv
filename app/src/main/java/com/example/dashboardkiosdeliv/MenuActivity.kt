package com.example.dashboardkiosdeliv

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.getbase.floatingactionbutton.FloatingActionButton

class MenuActivity : AppCompatActivity() {

    private lateinit var logout : FloatingActionButton
    private lateinit var changePassword : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        logout = findViewById(R.id.btn_logout)
        changePassword = findViewById(R.id.btn_ganti_password)

        logout.setOnClickListener {
            customDialog()
        }

        changePassword.setOnClickListener {
            intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

    }
        fun customDialog(){

            val btn_sudah = (findViewById<Button>(R.id.btn_logout_sudah))
            val btn_belum = (findViewById<Button>(R.id.btn_logout_belum))

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.popup_logout_layout)

            dialog.show()

//            btn_belum.setOnClickListener {
//                dialog.dismiss()
//            }
//            btn_sudah.setOnClickListener {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            }
        }
    }

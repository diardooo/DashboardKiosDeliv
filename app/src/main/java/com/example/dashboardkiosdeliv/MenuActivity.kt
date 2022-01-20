package com.example.dashboardkiosdeliv

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
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

        logout = findViewById(R.id.btn_float_logout)
        changePassword = findViewById(R.id.btn_float_ganti_password)

        logout.setOnClickListener {
            customDialog()
        }

        changePassword.setOnClickListener {
            var intent1 = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent1)
        }

    }
        fun customDialog(){

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_logout_layout,null)
            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(mDialogView)
            mDialogView.setBackgroundResource(R.drawable.popup_bg)
            val mAlertDialog = mBuilder.show()
            mDialogView.findViewById<Button>(R.id.btn_logout_sudah).setOnClickListener {
                mAlertDialog.dismiss()
                var intent2 = Intent(this, LoginActivity::class.java)
                startActivity(intent2)
            }
            mDialogView.findViewById<Button>(R.id.btn_logout_belum).setOnClickListener {
                mAlertDialog.dismiss()
            }

        }
    }

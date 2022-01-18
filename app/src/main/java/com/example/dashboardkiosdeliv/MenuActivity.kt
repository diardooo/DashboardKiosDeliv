package com.example.dashboardkiosdeliv

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.view.View
import android.widget.Button
import com.getbase.floatingactionbutton.FloatingActionButton

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        logout()

    }

    fun logout(){

        val logout = (findViewById<FloatingActionButton>(R.id.btn_logout))
        val btn_sudah = (findViewById<Button>(R.id.btn_logout_sudah))
        val btn_belum = (findViewById<Button>(R.id.btn_logout_belum))

        logout.setOnClickListener {

            val view = View.inflate(this, R.layout.popup_logout_layout, null)

            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)

            if (btn_belum.callOnClick()) {
                dialog.dismiss()
            }
            if (btn_sudah.callOnClick()){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
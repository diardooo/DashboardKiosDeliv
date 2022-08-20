package com.example.dashboardkiosdeliv

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.cardview.widget.CardView
import com.getbase.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.popup_logout_layout.*

class MenuStaffMarketingActivity : AppCompatActivity() {

    private lateinit var logout : FloatingActionButton
    private lateinit var changePassword : FloatingActionButton
    private lateinit var btnDashboard : CardView
    private lateinit var btnProgresLoket : CardView
    private lateinit var btnProfile : CardView
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_staff_marketing)

        profile = getSharedPreferences("login_session", MODE_PRIVATE)

        logout = findViewById(R.id.btn_float_logout_staff)
        changePassword = findViewById(R.id.btn_float_ganti_password_staff)
        btnDashboard = findViewById(R.id.btn_dashboard_staff)
        btnProgresLoket = findViewById(R.id.btn_progres_loket_staff)
        btnProfile = findViewById(R.id.btn_profile_staff)

        //LOGOUT
        logout.setOnClickListener {
            customDialog()
        }

        //CHANGE PASSWORD
        changePassword.setOnClickListener {
            var intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        //DASHBOARD
        btnDashboard.setOnClickListener {
            var intent = Intent(this, DashboardStaffMarketingActivity::class.java)
            startActivity(intent)
        }

        //PROGRES LOKET
        btnProgresLoket.setOnClickListener {
            var intent = Intent(this, ProgressLoketActivity::class.java)
            startActivity(intent)
        }

        //PROFILE
        btnProfile.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun customDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_logout_layout,null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(mDialogView)
        mDialogView.setBackgroundResource(R.drawable.popup_bg)
        val mAlertDialog = mBuilder.show()

        mDialogView.findViewById<Button>(R.id.btn_logout_sudah).setOnClickListener {
            //Menghapus session
            profile.edit().clear().commit()
            mAlertDialog.dismiss()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        mDialogView.findViewById<Button>(R.id.btn_logout_belum).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    override fun onBackPressed() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_logout_layout2,null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(mDialogView)
        mDialogView.setBackgroundResource(R.drawable.popup_bg)
        val mAlertDialog = mBuilder.show()

        mDialogView.findViewById<Button>(R.id.btn_logout_sudah).setOnClickListener {
            mAlertDialog.dismiss()
            finishAffinity()

        }

        mDialogView.findViewById<Button>(R.id.btn_logout_belum).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

}
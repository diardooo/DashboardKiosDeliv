package com.example.dashboardkiosdeliv

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.getbase.floatingactionbutton.FloatingActionButton

class MenuDirekturActivity : AppCompatActivity() {

    private lateinit var logout : FloatingActionButton
    private lateinit var changePassword : FloatingActionButton
    private lateinit var btnDashboard : CardView
    private lateinit var btnProgresLoket : CardView
    private lateinit var btnInfoDeposit : CardView
    private lateinit var btnProfile : CardView
    private lateinit var profile : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_direktur)

        profile = getSharedPreferences("login_session", MODE_PRIVATE)

        logout = findViewById(R.id.btn_float_logout_direktur)
        changePassword = findViewById(R.id.btn_float_ganti_password_direktur)
        btnDashboard = findViewById(R.id.btn_dashboard_direktur)
        btnProgresLoket = findViewById(R.id.btn_progres_loket_direktur)
        btnInfoDeposit = findViewById(R.id.btn_deposit_direktur)
        btnProfile = findViewById(R.id.btn_profile_direktur)

        //LOGOUT
        logout.setOnClickListener {
            customDialog()
        }

        //CHANGE PASSWORD
        changePassword.setOnClickListener {
            var intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        //DASHBOARD
        btnDashboard.setOnClickListener {
            var intent = Intent(this, DashboardDirekturActivity::class.java)
            startActivity(intent)
        }

        //PROGRES LOKET
        btnProgresLoket.setOnClickListener {
            var intent = Intent(this, ProgressLoketActivity::class.java)
            startActivity(intent)
        }

        //INFORMASI DEPOSIT
        btnInfoDeposit.setOnClickListener {
            var intent = Intent(this,InformasiDepositActivity::class.java)
            startActivity(intent)
        }

        //PROFILE
        btnProfile.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_logout_layout,null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(mDialogView)
        mDialogView.setBackgroundResource(R.drawable.popup_bg)
        val mAlertDialog = mBuilder.show()

        mDialogView.findViewById<Button>(R.id.btn_logout_sudah).setOnClickListener {
            mAlertDialog.dismiss()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        mDialogView.findViewById<Button>(R.id.btn_logout_belum).setOnClickListener {
            mAlertDialog.dismiss()
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
    }

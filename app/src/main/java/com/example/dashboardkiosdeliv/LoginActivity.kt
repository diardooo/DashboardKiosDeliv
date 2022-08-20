package com.example.dashboardkiosdeliv

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dashboardkiosdeliv.databinding.ActivityLoginBinding
import com.example.dashboardkiosdeliv.model.ResponseLogin
import com.example.dashboardkiosdeliv.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var binding : ActivityLoginBinding? = null
    private var email : String = ""
    private var password : String = ""
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //Cek session
        profile = getSharedPreferences("login_session", MODE_PRIVATE)

        if (profile.getString("email", null) != null){
            if (profile.getString("level", null) == "1"){
                startActivity(Intent(this,MenuDirekturActivity::class.java))
                finish()
            }
            if (profile.getString("level", null) == "2"){
                startActivity(Intent(this, MenuStaffMarketingActivity::class.java))
                finish()
            }
        }

        //Event login
        binding!!.btnLogin.setOnClickListener {
            email = binding!!.etUsernameLogin.text.toString()
            password = binding!!.etPasswordLogin.text.toString()

            when {
                email == "" -> {
                    binding!!.etUsernameLogin.error = "Email tidak boleh kosong"
                }
                password == "" -> {
                    binding!!.etPasswordLogin.error = "Password tidak boleh kosong"
                }
                else -> {
                    getData()
                }
            }
        }
    }

    private fun getData(){

        RetrofitClient.myApiClient().login(email, password).enqueue(object : Callback<ResponseLogin>{

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                Log.e("response", response.body()?.message.toString())
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {

                        //Membuat session
                        getSharedPreferences("login_session", MODE_PRIVATE)
                            .edit()
                            .putString("email", response.body()?.user?.email)
                            .putString("name", response.body()?.user?.name)
                            .putString("level", response.body()?.user?.level)
                            .putString("photo", response.body()?.user?.photo)
                            .apply()

                        if (response.body()?.user?.level == "1"){
                            startActivity(Intent(this@LoginActivity, MenuDirekturActivity::class.java))
                            finish()
                        }
                        if (response.body()?.user?.level == "2"){
                            startActivity(Intent(this@LoginActivity, MenuStaffMarketingActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login gagal, periksa kembali email dan password Anda",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login gagal, terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("pesan error","${t.message}")

                Toast.makeText(this@LoginActivity,"Login gagal, periksa kembali email dan password Anda",Toast.LENGTH_SHORT).show()            }
        })
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
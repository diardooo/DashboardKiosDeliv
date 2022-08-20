package com.example.dashboardkiosdeliv

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dashboardkiosdeliv.databinding.ActivityChangePasswordBinding
import com.example.dashboardkiosdeliv.model.ResponseChangePass
import com.example.dashboardkiosdeliv.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private var binding : ActivityChangePasswordBinding? = null
    private var email : String = ""
    private var password_lama : String = ""
    private var password_baru : String = ""
    private  var konfirmasi_password : String = ""
    private lateinit var profile : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        profile = getSharedPreferences("login_session", MODE_PRIVATE)

        back()

//        email = text_email.toString()
//        password_lama = text_password_lama.toString()
//        password_baru = text_password_baru.toString()
//        konfirmasi_password = text_konfirmasi_password.toString()

        binding!!.btnGantiPassword.setOnClickListener {
            email = binding!!.textEmail.text.toString()
            password_lama = binding!!.textPasswordLama.text.toString()
            password_baru = binding!!.textPasswordBaru.text.toString()
            konfirmasi_password = binding!!.textKonfirmasiPassword.text.toString()

            when{
                email == "" -> {
                    binding!!.textEmail.error = "Mohon masukkan email terlebih dahulu"
                }
                password_lama == "" -> {
                    binding!!.textPasswordLama.error = "Mohon masukkan password lama terlebih dahulu"
                }
                password_baru == "" -> {
                    binding!!.textPasswordBaru.error = "Mohon masukkan password baru terlebih dahulu"
                }
                konfirmasi_password == "" -> {
                    binding!!.textKonfirmasiPassword.error = "Mohon masukkan konfirmasi password terlebih dahulu"
                }
                password_baru != konfirmasi_password -> {
                    binding!!.textKonfirmasiPassword.error = "Password baru tidak cocok"
                } else -> {
                    changePasswowrd()
                }
            }
        }
    }

    private fun changePasswowrd(){
        RetrofitClient.myApiClient().changePassword(email,password_lama,password_baru).enqueue(object : Callback<ResponseChangePass>{
            override fun onResponse(
                call: Call<ResponseChangePass>,
                response: Response<ResponseChangePass>
            ) {
                if (response.isSuccessful){
                    if (response.body()?.success == true) {
                        response.body()?.let {
                            val message = it.message
                            if (message.equals("success")){
                                Toast.makeText(this@ChangePasswordActivity, "Password Berhasil Diubah", Toast.LENGTH_SHORT).show()

                                //Menghapus session user
                                profile.edit().clear().commit()
                                val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@ChangePasswordActivity, "Data user tidak cocok", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@ChangePasswordActivity, "Data user tidak cocok", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this@ChangePasswordActivity, "Data user tidak cocok", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseChangePass>, t: Throwable) {
                Log.e("pesan error","${t.message}")
            }

        })
    }

    private fun back(){
        var back = findViewById<ImageView>(R.id.img_back_chng_pass)
        back.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

}
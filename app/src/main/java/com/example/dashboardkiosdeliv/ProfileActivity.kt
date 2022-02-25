package com.example.dashboardkiosdeliv

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.dashboardkiosdeliv.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private var binding : ActivityProfileBinding? = null
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var btnBack = findViewById<ImageView>(R.id.img_back_profile)
        profile = getSharedPreferences("login_session", MODE_PRIVATE)


        //Event
        btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        //Menampilkan data profile
        binding?.tvNamaUser?.text = profile.getString("nama", null)
        binding?.tvEmail?.text = profile.getString("email", null)
        binding?.tvLevelUser?.text = if (profile.getString("level", null) == "1") "DIREKTUR" else "STAFF MARKETING"

        if (binding?.tvLevelUser?.text == "DIREKTUR"){
            binding?.tvLevelUser?.setTextColor(Color.parseColor("#F44336"))
        } else{
            binding?.tvLevelUser?.setTextColor(Color.parseColor("#2196F3"))
        }

        //Menampilkan foto profile dengan picasso
        Picasso.get().load(profile.getString("foto", null)).into(binding?.imgProfile)
    }
}
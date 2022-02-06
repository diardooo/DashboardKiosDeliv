package com.example.dashboardkiosdeliv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.dashboardkiosdeliv.adapter.OnBoardingViewPagerAdapter
import com.example.dashboardkiosdeliv.model.OnBoardingData
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

class BoardingActivity : AppCompatActivity() {

    var onBoardingViewPagerAdapter:OnBoardingViewPagerAdapter? = null
    var tabLayout: TabLayout? = null
    var onBoardingViewPager : ViewPager? = null
    var next : TextView? = null
    var position = 0
    var sharedPreferences : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (restorePrefData()){
            val i = Intent(application, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        supportActionBar!!.hide()

        setContentView(R.layout.activity_boarding)

        tabLayout = findViewById(R.id.tabIndicator)
        next = findViewById(R.id.btn_next)

        // Add some data to model class

        val onBoardingData:MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(OnBoardingData("DASHBOARD","Berisi informasi jumlah transaksi dan vendor Kios Deliv",R.drawable.onboarding_transaksi))
        onBoardingData.add(OnBoardingData("LOKET","Terdapat informasi progres loket Kios Deliv",R.drawable.onboarding_loket))
        onBoardingData.add(OnBoardingData("DEPOSIT","Berisi informasi jumlah deposit user Kios Deliv", R.drawable.onboarding_deposit))

        setOnboardingViewPagerAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        next?.setOnClickListener {
            if (position < onBoardingData.size){
                position++
                onBoardingViewPager!!.currentItem = position
            }
            if(position == onBoardingData.size){
                savePrefData()
                val i = Intent(application, LoginActivity::class.java)
                startActivity(i)
            }
        }

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if(tab.position == onBoardingData.size - 1){
                    next!!.text = "Get Started"
                } else {
                    next!!.text = "Next"
                }
            }
        })

    }

    private fun setOnboardingViewPagerAdapter(onBoardingData: List<OnBoardingData>){

        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)
    }

    private fun savePrefData(){

        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }
    private fun restorePrefData(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)
    }
}
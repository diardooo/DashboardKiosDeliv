package com.example.dashboardkiosdeliv

import android.app.Activity
import android.app.AlertDialog
import kotlinx.android.synthetic.main.activity_dashboard_direktur.*

class LoadingDialog (val mActivity : Activity) {

    private lateinit var isDialog: AlertDialog

    fun startLoading(){
        //Set View
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_layout, null)
        dialogView.setBackgroundResource(R.drawable.popup_bg)

        //Set Dialog
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog = builder.create()
        isDialog.show()
    }

    fun dismissLoading(){
        isDialog.dismiss()
    }
}
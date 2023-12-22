package com.dicoding.capstone.utils

import android.app.Activity
import android.app.AlertDialog
import com.dicoding.capstone.R

class LoadingDialog(private val contextActivity: Activity) {
    private lateinit var alertDialog: AlertDialog

    fun showLoadingDialog() {
        val dialogBuilder = AlertDialog.Builder(contextActivity)

        val layoutInflater = contextActivity.layoutInflater
        dialogBuilder.setView(layoutInflater.inflate(R.layout.custom_dialog, null))
        dialogBuilder.setCancelable(true)

        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun hideLoadingDialog() {
        alertDialog.dismiss()
    }
}
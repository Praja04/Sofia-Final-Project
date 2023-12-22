package com.dicoding.capstone.utils

import android.content.Context

internal class MyPreference(context: Context) {

    private val preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun setLoginStatus(status: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(loginStatus, status)
        editor.apply()
    }

    fun getLoginStatus(): Boolean {
        val status = preferences.getBoolean(loginStatus, false)
        return status
    }

    companion object {
        private const val prefName = "my_preference"
        private const val loginStatus = "login_status"
        private const val absenceStatus = "absence_status"
    }
}
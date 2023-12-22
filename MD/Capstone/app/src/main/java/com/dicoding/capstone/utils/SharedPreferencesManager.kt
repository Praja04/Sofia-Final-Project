package com.dicoding.capstone.utils

import android.content.Context
import com.dicoding.capstone.data.response.LoginResponse

class SharedPreferencesManager(private val appContext: Context) {
    private val sharedPrefs = appContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    companion object {
        const val TOKEN_KEY = "user_token"
    }

    fun saveUserToken(value: LoginResponse) {
        val editor = sharedPrefs.edit()
        editor.putString(TOKEN_KEY, value.token)
        editor.apply()
    }

    fun getUserToken(): String? {
        return sharedPrefs.getString(TOKEN_KEY, "")
    }

    fun clearUserToken() {
        val editor = sharedPrefs.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}

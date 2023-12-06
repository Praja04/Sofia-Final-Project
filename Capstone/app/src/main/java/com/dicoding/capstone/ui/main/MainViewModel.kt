package com.dicoding.capstone.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.utils.MyPreference

class MainViewModel: ViewModel() {

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    fun setLoginStatus(context: Context, status: Boolean) {
        val myPreference = MyPreference(context)
        myPreference.setLoginStatus(status)
    }

    fun getLoginStatus(context: Context) {
        val myPreference = MyPreference(context)
        _loginStatus.value = myPreference.getLoginStatus()
    }

}
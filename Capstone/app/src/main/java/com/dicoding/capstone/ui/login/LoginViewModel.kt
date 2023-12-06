package com.dicoding.capstone.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.utils.MyPreference

class LoginViewModel: ViewModel() {

    /**
     * edit or add more username and password do u want to use
     * you can add role or additional information to complexity
     */
    private val usernameDummy = "username@username.com"
    private val passwordDummy = "123456"

    private val _loginMessage = MutableLiveData<String?>()
    val loginMessage: LiveData<String?> = _loginMessage

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * like validation in api or client side, you can check input username,password,etc in here
     * and you can add additional informational if validation success like result message,token,etc
     */
    fun checkLogin(username: String, password:String) {
        _isLoading.value = true
        if (username != usernameDummy) {
            _isLoading.value = false
            _loginMessage.value = "Maaf, Username yang dimasukkan salah. \n" +
                    "mohon coba lagi."
        } else if (password != passwordDummy) {
            _isLoading.value = false
            _loginMessage.value = "Maaf, Password yang dimasukkan salah. \n" +
                    "mohon coba lagi."
        } else {
            _isLoading.value = false
            _loginMessage.value = "Login berhasil!"
            _loginStatus.value = true
        }
    }

    fun setLoginStatus(context: Context, status: Boolean) {
        val myPreference = MyPreference(context)
        myPreference.setLoginStatus(status)
    }

    fun getLoginStatus(context: Context) {
        val myPreference = MyPreference(context)
        _loginStatus.value = myPreference.getLoginStatus()
    }
}
package com.dicoding.capstone.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.data.UserRepository
import com.dicoding.capstone.utils.LoadingDialog
import com.dicoding.capstone.utils.MyPreference
import com.dicoding.capstone.utils.SharedPreferencesManager

class LoginViewModel(private val userRepository: UserRepository): ViewModel(){
    fun initiateLogin(
        email: String,
        password: String,
        context: LoginActivity,
        loadingIndicator: LoadingDialog,
        userPreferences: SharedPreferencesManager
    ) =
        userRepository.loginUser(email, password, context, loadingIndicator, userPreferences)
}

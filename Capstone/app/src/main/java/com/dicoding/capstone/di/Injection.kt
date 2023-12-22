package com.dicoding.capstone.di

import android.content.Context
import com.dicoding.capstone.data.UserRepository
import com.dicoding.capstone.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        //Untuk mendapatkan ApiService
        val apiService = ApiConfig.getApiServices()
        return UserRepository.getInstance(apiService)
    }
}
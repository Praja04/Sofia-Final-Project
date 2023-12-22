package com.dicoding.capstone.ui.home

import androidx.lifecycle.ViewModel
import com.dicoding.capstone.data.UserRepository

class HomeViewModel (private val userRepository: UserRepository) : ViewModel()  {
    fun getAllProductsItem(token: String) = userRepository.getAllProducts(token)
}
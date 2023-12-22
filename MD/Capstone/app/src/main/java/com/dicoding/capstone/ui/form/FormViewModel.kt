package com.dicoding.capstone.ui.form

import androidx.lifecycle.ViewModel
import com.dicoding.capstone.data.UserRepository
import com.dicoding.capstone.utils.LoadingDialog
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FormViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun createItem(token: String, namabarang: String, jumlah: Int, harga: String, loadingDialog: LoadingDialog, activity: FormActivity) =
        userRepository.createItem(token, namabarang, jumlah, harga, loadingDialog, activity)
}
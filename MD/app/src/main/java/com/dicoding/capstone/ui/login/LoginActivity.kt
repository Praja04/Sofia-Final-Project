package com.dicoding.capstone.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityLoginBinding
import com.dicoding.capstone.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initObserver()
        initListener()

    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun initObserver() {

        viewModel.getLoginStatus(this)

        viewModel.isLoading.observe(this) { isloading ->
            showLoading(isloading)
        }
        viewModel.loginMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                showSnackbar(message)
            }
        }
        viewModel.loginStatus.observe(this) { loginStatus ->
            if (loginStatus) {
                viewModel.setLoginStatus(this@LoginActivity, loginStatus)
                val intentHome = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentHome)
                finish()
            }
        }
    }

    private fun initListener() {

        binding.btnLogin.setOnClickListener {
            initLogin()
        }
    }

    private fun initLogin() {
        val textUsername = binding.etLoginUsername.text.toString()
        val textPassword = binding.etLoginPassword.text.toString()

        viewModel.checkLogin(textUsername, textPassword)
    }

    private fun showSnackbar(message: String) {

        val snackbar = Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    private fun showLoading(isLoading: Boolean) {

        binding.progressBar.isVisible = isLoading
    }
}
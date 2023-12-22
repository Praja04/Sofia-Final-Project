package com.dicoding.capstone.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityLoginBinding
import com.dicoding.capstone.ui.ViewModelFactory
import com.dicoding.capstone.ui.main.MainActivity
import com.dicoding.capstone.ui.register.RegisterActivity
import com.dicoding.capstone.utils.LoadingDialog
import com.dicoding.capstone.utils.SharedPreferencesManager
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val userPreferences = SharedPreferencesManager(this)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
        val viewModel: LoginViewModel by viewModels {
            factory
        }
        val actionBar = supportActionBar
        actionBar?.hide()
        val loadingDialog: LoadingDialog = LoadingDialog(this@LoginActivity)

        binding?.apply {
            Log.d(ACTIVITY_NAME, etLoginUsername.text.toString())
            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btnLogin.setOnClickListener {
                if (etLoginUsername.text!!.isNotEmpty()) {
                    loadingDialog.showLoadingDialog()
                    viewModel.initiateLogin(
                        etLoginUsername.text.toString(),
                        etLoginPassword.text.toString(),
                        this@LoginActivity,
                        loadingDialog,
                        userPreferences
                    )
                } else {
                    Toast.makeText(this@LoginActivity, resources.getString(R.string.empty_input), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    companion object {
        const val ACTIVITY_NAME = "LoginActivity"
    }
}
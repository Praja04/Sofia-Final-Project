package com.dicoding.capstone.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityMainBinding
import com.dicoding.capstone.ui.home.HomeActivity
import com.dicoding.capstone.ui.login.LoginActivity
import com.dicoding.capstone.ui.profile.ProfileActivity
import com.dicoding.capstone.ui.saldo.SaldoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHome()
        initSaldo()
        initProfile()

        initViewModel()
        initObserver()
        initListener()
    }

    private fun initHome(){
        binding.btnHome.setOnClickListener{
            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        }
    }

    private fun initSaldo(){
        binding.btnSaldo.setOnClickListener{
            val moveSaldo = Intent(this@MainActivity, SaldoActivity::class.java)
            startActivity(moveSaldo)
        }
    }

    private fun initProfile(){
        binding.btnProfile.setOnClickListener{
            val moveProfile = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(moveProfile)
        }
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun initObserver() {

        viewModel.getLoginStatus(this)

        viewModel.loginStatus.observe(this) { loginStatus ->
            if (!loginStatus) {
                initLogout()
            }
        }
    }

    private fun initListener() {

        binding.logout.setOnClickListener {
            viewModel.setLoginStatus(this@MainActivity, false)
            initLogout()
        }
    }

    private fun initLogout() {
        val intentLogin = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intentLogin)
        finish()
    }
}
package com.dicoding.capstone.ui.main

import android.app.AlertDialog
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
import com.dicoding.capstone.utils.SharedPreferencesManager

class MainActivity : AppCompatActivity() {

    private lateinit var userPreferencesManager: SharedPreferencesManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHome()
        initSaldo()
        initProfile()


        binding?.logout?.setOnClickListener {
            val logoutConfirmationDialog = AlertDialog.Builder(this)
            logoutConfirmationDialog.setMessage(resources.getString(R.string.logout_desc))
            logoutConfirmationDialog.setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                val sharedPrefs = SharedPreferencesManager(this)
                sharedPrefs.clearUserToken()
                val loginIntent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(loginIntent)
            }
            logoutConfirmationDialog.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
            }
            val dialog = logoutConfirmationDialog.create()
            dialog.show()
        }

        userPreferencesManager = SharedPreferencesManager(this)
        userPreferencesManager.getUserToken()

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

    override fun onRestart() {
        super.onRestart()
        userPreferencesManager.getUserToken()
    }

}
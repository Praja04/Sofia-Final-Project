package com.dicoding.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.capstone.databinding.ActivitySplashScreenBinding
import com.dicoding.capstone.ui.login.LoginActivity
import com.dicoding.capstone.ui.main.MainActivity
import com.dicoding.capstone.utils.SharedPreferencesManager

class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.hide()
        val userPref = SharedPreferencesManager(this)

        if (userPref.getUserToken() != null && userPref.getUserToken() != "") {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                finish()
                startActivity(intent)
            }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }, 1000)
        }
    }
}

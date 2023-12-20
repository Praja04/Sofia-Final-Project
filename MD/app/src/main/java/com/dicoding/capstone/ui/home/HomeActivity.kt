package com.dicoding.capstone.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.capstone.databinding.ActivityHomeBinding
import com.dicoding.capstone.ui.form.FormActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val formIntent = Intent(this, FormActivity::class.java)
            startActivity(formIntent)

        }
    }
}
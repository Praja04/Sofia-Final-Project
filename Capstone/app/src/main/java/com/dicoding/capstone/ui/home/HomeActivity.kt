package com.dicoding.capstone.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstone.databinding.ActivityHomeBinding
import com.dicoding.capstone.ui.ViewModelFactory
import com.dicoding.capstone.ui.form.FormActivity
import com.dicoding.capstone.utils.SharedPreferencesManager
import com.dicoding.capstone.data.Result
import com.dicoding.capstone.ui.detail.DetailActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: HomeViewModel by viewModels {
        factory
    }
    private lateinit var listItemAdapter: ListItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fab?.setOnClickListener {
            val formIntent = Intent(this, FormActivity::class.java)
            startActivity(formIntent)
        }

        val actionBar = supportActionBar
        actionBar?.hide()

        listItemAdapter = ListItemAdapter { data ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_NAME, data.namaBarang)
            intent.putExtra(DetailActivity.EXTRA_JUMLAH, data.jumlah)
            intent.putExtra(DetailActivity.EXTRA_HARGA, data.harga)
            startActivity(intent)
        }

        sharedPreferencesManager = SharedPreferencesManager(this)
        val token = sharedPreferencesManager.getUserToken()

        if (token != null) {
            showListProducts(token)
        }
    }

    private fun showListProducts(token: String) {
        viewModel.getAllProductsItem("Bearer $token").observe(this) { result ->

            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.shimmer?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.shimmer?.visibility = View.GONE
                        val data = result.data
                        listItemAdapter.submitList(data)
                        binding?.rvItem?.apply {
                            layoutManager = LinearLayoutManager(this@HomeActivity)
                            Handler().postDelayed({
                                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                                    0,
                                    0
                                )
                            }, 1000)

                            setHasFixedSize(true)
                            adapter = listItemAdapter
                        }
                    }

                    is Result.Error -> {
                        binding?.shimmer?.visibility = View.GONE
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRestart() {
        super.onRestart()
        val token = sharedPreferencesManager.getUserToken()
        if (token != null) {
            showListProducts(token)
        }
    }
}
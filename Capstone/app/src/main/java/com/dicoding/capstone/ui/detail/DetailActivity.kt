package com.dicoding.capstone.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = resources.getString(R.string.detail_item)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        var name: String? = intent.getStringExtra(EXTRA_NAME)
        var jumlah: String? = intent.getStringExtra(EXTRA_JUMLAH)
        var harga: String? = intent.getStringExtra(EXTRA_HARGA)

        binding?.apply {
            tvItem.text = name
            tvJumlah.text = jumlah
            tvHarga.text = harga
        }

    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_JUMLAH = "extra_jumlah"
        const val EXTRA_HARGA = "extra_harga"
    }
}
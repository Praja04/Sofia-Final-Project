package com.dicoding.capstone.ui.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityFormBinding
import com.dicoding.capstone.ui.ViewModelFactory
import com.dicoding.capstone.utils.LoadingDialog
import com.dicoding.capstone.utils.SharedPreferencesManager

class FormActivity : AppCompatActivity() {

    private var _binding: ActivityFormBinding? = null
    private val binding get() = _binding
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.add_item)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@FormActivity)
        val viewModel: FormViewModel by viewModels {
            factory
        }
        val loadingDialog: LoadingDialog = LoadingDialog(this@FormActivity)
        sharedPreferencesManager = SharedPreferencesManager(this)
        val token = sharedPreferencesManager.getUserToken()

        binding?.apply {
            btnSaveProduct.setOnClickListener{
                loadingDialog.showLoadingDialog()
                if (token != null){
                    saveItem(viewModel, token, loadingDialog)
                }
            }
        }
    }

    fun saveItem(
        viewModel: FormViewModel,
        token: String,
        loadingDialog: LoadingDialog
    ){
        binding?.etProdukName?.text.toString()
        binding?.etJumlahProduk?.text.toString().toInt()
        binding?.etHarga?.text.toString()
        viewModel.createItem(
            "Bearer $token",
            namabarang = String(),
            jumlah = String().toInt(),
            harga = String(),
            loadingDialog,
            this@FormActivity
        )
    }

    companion object {
        const val NAME_ACTIVITY = "FormActivity"
    }
}
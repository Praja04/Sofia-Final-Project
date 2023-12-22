package com.dicoding.capstone.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.ActivityRegisterBinding
import com.dicoding.capstone.ui.ViewModelFactory
import com.dicoding.capstone.utils.LoadingDialog

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = resources.getString(R.string.register)
        val loadingDialog: LoadingDialog = LoadingDialog(this@RegisterActivity)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@RegisterActivity)
        val viewModel: RegisterViewModel by viewModels {
            factory
        }
        binding?.apply {
            signupButton.setOnClickListener {
                if (nameEditText.text!!.isNotEmpty() && emailEditText.text!!.isNotEmpty() && passwordEditText.text!!.isNotEmpty()) {
                    loadingDialog.showLoadingDialog()
                    viewModel.initiateRegistration(
                        nameEditText.text.toString(),
                        emailEditText.text.toString(),
                        passwordEditText.text.toString(),
                        confpasswordEditText.text.toString(),
                        this@RegisterActivity,
                        loadingDialog
                    )
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        resources.getString(R.string.empty_input),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    companion object {
        const val NAME_ACTIVITY = "RegisterActivity"
    }
}
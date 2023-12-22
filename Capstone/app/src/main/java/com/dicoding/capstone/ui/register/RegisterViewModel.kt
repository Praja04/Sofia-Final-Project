package com.dicoding.capstone.ui.register

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dicoding.capstone.data.UserRepository
import com.dicoding.capstone.data.response.RegisterResponse
import com.dicoding.capstone.data.retrofit.ApiConfig
import com.dicoding.capstone.utils.LoadingDialog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun initiateRegistration(
        name: String,
        email: String,
        password: String,
        confPassword: String,
        context: RegisterActivity,
        loadingIndicator: LoadingDialog
    ) {
        val client = ApiConfig.getApiServices().registerUser(name, email, password, confPassword)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loadingIndicator.hideLoadingDialog()
                        Log.d(RegisterActivity.NAME_ACTIVITY, responseBody.toString())
                        Toast.makeText(context, responseBody.response, Toast.LENGTH_SHORT)
                            .show()
                        context.finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    loadingIndicator.hideLoadingDialog()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                loadingIndicator.hideLoadingDialog()
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d(RegisterActivity.NAME_ACTIVITY, t.message.toString())
            }
        })
    }
}

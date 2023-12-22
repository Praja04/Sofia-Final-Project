package com.dicoding.capstone.data

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.capstone.data.response.CreateItemResponse
import com.dicoding.capstone.data.response.GetItemResponse
import com.dicoding.capstone.data.response.ListBarangItem
import com.dicoding.capstone.data.response.LoginResponse
import com.dicoding.capstone.data.retrofit.ApiService
import com.dicoding.capstone.ui.form.FormActivity
import com.dicoding.capstone.ui.login.LoginActivity
import com.dicoding.capstone.ui.main.MainActivity
import com.dicoding.capstone.utils.LoadingDialog
import com.dicoding.capstone.utils.SharedPreferencesManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor (
    private val apiService: ApiService
) {

    fun getAllProducts(
        token: String
    ): LiveData<Result<List<ListBarangItem>>> {
        val listProducts =
            MediatorLiveData<Result<List<ListBarangItem>>>()

        listProducts.postValue(Result.Loading)
        val client = apiService.getAllItem(token)
        client.enqueue(object : Callback<GetItemResponse> {
            override fun onResponse(
                call: Call<GetItemResponse>,
                response: Response<GetItemResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listProducts.value =
                            Result.Success(responseBody.listBarang)
                    }
                } else {
                    listProducts.postValue(Result.Error("Error ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetItemResponse>, t: Throwable) {
                listProducts.postValue(Result.Error(t.message.toString()))
            }
        })

        return listProducts

    }

    fun createItem(
        token: String,
        namabarang: String,
        jumlah: Int,
        harga: String,
        loadingDialog: LoadingDialog,
        activity: FormActivity
    ) {
        val client = apiService.createItem(token, namabarang, jumlah, harga)
        client.enqueue(object : Callback<CreateItemResponse> {
            override fun onResponse(
                call: Call<CreateItemResponse>,
                response: Response<CreateItemResponse>
            ) {
                if (response.isSuccessful) {
                    loadingDialog.hideLoadingDialog()
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val message = responseBody.msg
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        activity.finish()
                    }
                } else {
                    loadingDialog.hideLoadingDialog()
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody).getString("message")
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateItemResponse>, t: Throwable) {
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_SHORT).show()
                loadingDialog.hideLoadingDialog()
            }
        })
    }

    fun loginUser(
        email: String,
        password: String,
        activity: LoginActivity,
        loadingDialog: LoadingDialog,
        userPref: SharedPreferencesManager
    ) {
        val client = apiService.loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loadingDialog.hideLoadingDialog()
                        userPref.saveUserToken(responseBody.copy())
                        val intent = Intent(activity, MainActivity::class.java)
                        activity.finish()
                        activity.startActivity(intent)
                    }
                } else {
                    loadingDialog.hideLoadingDialog()
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loadingDialog.hideLoadingDialog()
            }

        })
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }

}


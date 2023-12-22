package com.dicoding.capstone.data.retrofit

import com.dicoding.capstone.data.response.CreateItemResponse
import com.dicoding.capstone.data.response.GetItemResponse
import com.dicoding.capstone.data.response.LoginResponse
import com.dicoding.capstone.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    // endpoint Register
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun registerUser(
        @Field("nama") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confPassword") confPassword: String
    ): Call<RegisterResponse>

    // endpoint login
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    //endpoint get produk
    @GET("products")
    fun getAllItem(
        @Header("Authorization") token: String
    ): Call<GetItemResponse>

    //endpoint create produk
    @FormUrlEncoded
    @POST("products")
    fun createItem(
        @Header("Authorization") token: String,
        @Field("nama_barang") namabarang: String,
        @Field("jumlah") jumlah: Int,
        @Field("harga") harga: String
    ) : Call<CreateItemResponse>
}
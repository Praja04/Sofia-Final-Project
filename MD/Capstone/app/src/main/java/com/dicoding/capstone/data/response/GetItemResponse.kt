package com.dicoding.capstone.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetItemResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("list-barang")
	val listBarang: List<ListBarangItem>,

	@field:SerializedName("error")
	val error: String
) : Parcelable

@Parcelize
data class ListBarangItem(

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("harga")
	val harga: String,

	@field:SerializedName("item_id")
	val itemId: Int,

	@field:SerializedName("nama_barang")
	val namaBarang: String
): Parcelable

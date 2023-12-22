package com.dicoding.capstone.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.dicoding.capstone.R
import com.dicoding.capstone.data.response.ListBarangItem
import com.dicoding.capstone.databinding.ItemHomeBinding

class ListItemAdapter(private val onListProductsClick: (ListBarangItem) -> Unit) :
    ListAdapter<ListBarangItem, ListItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onListProductsClick(data)
        }
    }

    class MyViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(products: ListBarangItem) {
            binding.tvItem.text = products.namaBarang
            binding.tvJumlah.text = products.jumlah.toString()
            binding.tvHarga.text = products.harga
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.img)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListBarangItem> =
            object : DiffUtil.ItemCallback<ListBarangItem>() {
                override fun areItemsTheSame(
                    oldItem: ListBarangItem,
                    newItem: ListBarangItem
                ): Boolean {
                    return oldItem.namaBarang == newItem.namaBarang
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: ListBarangItem,
                    newItem: ListBarangItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
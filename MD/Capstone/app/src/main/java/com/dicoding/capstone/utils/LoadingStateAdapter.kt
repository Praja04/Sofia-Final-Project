package com.dicoding.capstone.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retryAction: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    class LoadingStateViewHolder(private val itemBinding: ItemLoadingBinding, retryAction: () -> Unit) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.retryButton.setOnClickListener { retryAction.invoke() }
        }

        fun handleLoadState(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                itemBinding.errorMsg.text = loadState.error.localizedMessage
            }
            itemBinding.progressBar.isVisible = loadState is LoadState.Loading
            itemBinding.retryButton.isVisible = loadState is LoadState.Error
            itemBinding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.handleLoadState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val itemBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(itemBinding, retryAction)
    }
}

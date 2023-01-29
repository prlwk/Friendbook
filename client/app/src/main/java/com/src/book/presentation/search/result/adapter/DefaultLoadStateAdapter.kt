package com.src.book.presentation.search.result.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.LoadingSearchResultBinding

class DefaultLoadStateAdapter(private val onClickTryAgain: () -> Unit) :
    LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {

    class Holder(
        private val binding: LoadingSearchResultBinding,
        private val onClickTryAgain: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.layoutError.tvUpdate.setOnClickListener { onClickTryAgain }
        }

        fun bind(loadState: LoadState) = with(binding) {
            Log.d(
                "DefaultLoadState",
                "error ${loadState is LoadState.Error}, loading ${loadState is LoadState.Loading} "
            )
            binding.layoutError.tvError.isVisible = loadState is LoadState.Error
            binding.layoutError.tvUpdate.isVisible = loadState is LoadState.Error
            binding.pbLoading.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val binding = LoadingSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding, onClickTryAgain)
    }
}
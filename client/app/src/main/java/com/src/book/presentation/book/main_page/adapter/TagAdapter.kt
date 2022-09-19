package com.src.book.presentation.book.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.ViewHolderTagBinding

class TagAdapter :
    ListAdapter<String, TagAdapter.DataViewHolder>(StringDiffCallback()) {
    private lateinit var binding: ViewHolderTagBinding

    class DataViewHolder(binding: ViewHolderTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tag = binding.tvTag
        fun onBind(tag: String) {
            this.tag.text = tag
        }

        private val RecyclerView.ViewHolder.context
            get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderTagBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}
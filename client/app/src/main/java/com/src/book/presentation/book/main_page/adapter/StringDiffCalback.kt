package com.src.book.presentation.book.main_page.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class StringDiffCallback: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
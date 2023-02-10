package com.src.book.presentation.search.result.adapter

import androidx.recyclerview.widget.DiffUtil
import com.src.book.domain.model.author.AuthorList

class AuthorListCallback : DiffUtil.ItemCallback<AuthorList>() {
    override fun areItemsTheSame(oldItem: AuthorList, newItem: AuthorList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AuthorList, newItem: AuthorList): Boolean {
        return oldItem == newItem
    }
}
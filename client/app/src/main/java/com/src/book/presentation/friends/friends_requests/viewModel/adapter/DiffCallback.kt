package com.src.book.presentation.friends.friends_requests.viewModel.adapter

import androidx.recyclerview.widget.DiffUtil
import com.src.book.domain.model.friend.FriendRequest.FriendRequest

class DiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
    override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
        return oldItem == newItem
    }
}
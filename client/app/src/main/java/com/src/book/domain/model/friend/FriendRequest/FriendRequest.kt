package com.src.book.domain.model.friend.FriendRequest

data class FriendRequest(
    val id: Long,
    val name: String,
    val login: String,
    val imageUrl: String
)
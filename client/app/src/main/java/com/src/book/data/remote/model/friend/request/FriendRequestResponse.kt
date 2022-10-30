package com.src.book.data.remote.model.friend.request

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class FriendRequestResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("login") val login: String,
    @SerializedName("image") val imageUrl: String
)
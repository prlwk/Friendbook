package com.src.book.data.remote.model.friend.friend

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class FriendResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("login") val login: String,
    @SerializedName("countRateBooks") val countRateBooks: Int,
    @SerializedName("countReviews") val countReviews: Int,
    @SerializedName("countWantToReadBooks") val countWantToReadBooks: Int,
    @SerializedName("image") val image: String?
)
package com.src.book.data.remote.model.author.authorBook

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class AuthorBookResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
)
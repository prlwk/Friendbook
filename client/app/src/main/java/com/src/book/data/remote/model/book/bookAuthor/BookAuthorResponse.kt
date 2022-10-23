package com.src.book.data.remote.model.book.bookAuthor

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class BookAuthorResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("rating") val rating: Double,
    @SerializedName("linkCover") val linkCover: String?
)
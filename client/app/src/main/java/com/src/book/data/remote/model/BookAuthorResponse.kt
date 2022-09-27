package com.src.book.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BookAuthorResponse(
    @SerialName("id") val id: Long,
    @SerialName("rating") val rating: Double,
    @SerialName("linkCover") val linkCover: String
)
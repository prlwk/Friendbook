package com.src.book.data.remote.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class AuthorResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("yearsLife") val yearsLife: String,
    @SerialName("rating") val rating: Double,
    @SerialName("photoSrc") val photoSrc: String?,
    @SerialName("biography") val biography: String?,
    @SerialName("books") val books: List<BookAuthorResponse>?
)
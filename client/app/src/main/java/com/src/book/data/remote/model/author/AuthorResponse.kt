package com.src.book.data.remote.model.author

import com.src.book.data.remote.model.book.BookAuthorResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class AuthorResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("yearsLife") val yearsLife: String,
    @SerialName("rating") val rating: Double,
    @SerialName("photoSrc") val photoSrc: String?,
    @SerialName("biography") val biography: String?,
    @SerialName("books") val books: List<BookAuthorResponse>?
)
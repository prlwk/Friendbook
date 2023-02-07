package com.src.book.data.remote.model.author.author

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorResponse

@kotlinx.serialization.Serializable
class AuthorResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("yearsLife") val yearsLife: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("photoSrc") val photoSrc: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("books") val books: List<BookAuthorResponse>
)
package com.src.book.data.remote.model.book.bookList

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.author.authorBook.AuthorBookResponse
import com.src.book.data.remote.model.genre.GenreResponse

@kotlinx.serialization.Serializable
class BookListResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("linkCover") val linkCover: String?,
    @SerializedName("year") val year: String,
    @SerializedName("genres") val genres: List<GenreResponse>?,
    @SerializedName("authors") val authors: List<AuthorBookResponse>?,
    @SerializedName("grade") val grade: Int?,
    @SerializedName("isWantToRead") val isWantToRead: Boolean
)
package com.src.book.data.remote.model.book

import com.src.book.data.remote.model.GenreResponse
import com.src.book.data.remote.model.ReviewBookResponse
import com.src.book.data.remote.model.TagResponse
import com.src.book.data.remote.model.author.AuthorBookResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class BookResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("rating") val rating: Double,
    @SerialName("linkCover") val linkCover: String?,
    @SerialName("year") val year: String,
    @SerialName("genres") val genres: List<GenreResponse>?,
    @SerialName("authors") val authors: List<AuthorBookResponse>?,
    @SerialName("reviews") val reviews: List<ReviewBookResponse>?,
    @SerialName("description") val description: String?,
    @SerialName("tags") val tags: List<TagResponse>?
)
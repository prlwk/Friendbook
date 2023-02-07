package com.src.book.data.remote.model.book.book

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.author.authorBook.AuthorBookResponse
import com.src.book.data.remote.model.genre.GenreResponse
import com.src.book.data.remote.model.review.reviewBook.ReviewBookResponse
import com.src.book.data.remote.model.tag.TagResponse

@kotlinx.serialization.Serializable
class BookResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("linkCover") val linkCover: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("genres") val genres: List<GenreResponse>,
    @SerializedName("authors") val authors: List<AuthorBookResponse>,
    @SerializedName("reviews") val reviews: List<ReviewBookResponse>,
    @SerializedName("description") val description: String?,
    @SerializedName("tags") val tags: List<TagResponse>?,
    @SerializedName("grade") val grade: Int?,
    @SerializedName("isWantToRead") val isWantToRead: Boolean
)
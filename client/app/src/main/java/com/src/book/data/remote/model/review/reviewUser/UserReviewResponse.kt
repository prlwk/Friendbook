package com.src.book.data.remote.model.review.reviewUser

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.author.authorBook.AuthorBookResponse
@kotlinx.serialization.Serializable
class UserReviewResponse (
    @SerializedName("text")
    val text: String,
    @SerializedName("bookId")
    val bookId: Long,
    @SerializedName("bookName")
    val bookName: String,
    @SerializedName("authorForBookList")
    val authors: List<AuthorBookResponse>?,
    @SerializedName("date")
    var date: String,
    @SerializedName("grade")
    val grade: Int
)
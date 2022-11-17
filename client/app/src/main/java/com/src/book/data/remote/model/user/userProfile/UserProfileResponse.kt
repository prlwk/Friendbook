package com.src.book.data.remote.model.user.userProfile

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.book.bookList.BookListResponse
import com.src.book.data.remote.model.review.reviewUser.UserReviewResponse

@kotlinx.serialization.Serializable
class UserProfileResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("reviews")
    val reviews: List<UserReviewResponse>?,
    @SerializedName("ratedBooks")
    val ratedBooks: List<BookListResponse>?,
    @SerializedName("savingBooks")
    val savingBooks: List<BookListResponse>?,
    @SerializedName("countFriends")
    val countFriends: Long
)
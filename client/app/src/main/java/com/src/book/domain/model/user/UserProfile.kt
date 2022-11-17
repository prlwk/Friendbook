package com.src.book.domain.model.user

import com.src.book.domain.model.BookList
import com.src.book.domain.model.userReview.UserReview

data class UserProfile(
    val name: String,
    val login: String,
    val email: String,
    val image:String,
    val reviews: List<UserReview>?,
    val ratedBooks: List<BookList>?,
    val savingBooks: List<BookList>?,
    val countFriends:Long
)
package com.src.book.domain.model.userReview

import com.src.book.domain.author.AuthorBook

data class UserReview(
    val text: String,
    val bookId: Long,
    val bookName: String,
    val authors: List<AuthorBook>?,
    var date: String,
    val grade: Int
)
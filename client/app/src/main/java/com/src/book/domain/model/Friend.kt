package com.src.book.domain.model

data class Friend(
    val id: Long,
    val name: String,
    val login: String,
    val countRateBooks: Int,
    val countReviews: Int,
    val countWantToReadBooks: Int,
    val image: String?
)
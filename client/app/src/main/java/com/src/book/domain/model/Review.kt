package com.src.book.domain.model

data class Review(
    val id: Long,
    val username: String,
    val photoSrc: String?,
    val reviewText: String,
    val rating: Int
)
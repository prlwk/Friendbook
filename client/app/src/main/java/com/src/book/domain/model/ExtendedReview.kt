package com.src.book.domain.model

data class ExtendedReview(
    val id: Long,
    val username: String,
    val photoSrc: String?,
    val reviewText: String,
    var reviewDate: String,
    val rating: Int
)
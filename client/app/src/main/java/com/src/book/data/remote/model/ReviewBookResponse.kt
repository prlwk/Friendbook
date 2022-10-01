package com.src.book.data.remote.model

@kotlinx.serialization.Serializable
class ReviewBookResponse(
    val id: Long,
    val username: String,
    val photoSrc: String?,
    val reviewText: String,
    val rating: Int
)
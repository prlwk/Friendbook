package com.src.book.domain.author

data class AuthorList(
    val id: Long,
    val name: String,
    val rating: Double,
    val photoSrc: String?,
    val yearsLife: String,
)
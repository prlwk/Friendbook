package com.src.book.domain.model

data class Author(
    val id: Long,
    val name: String,
    val yearsLife: String,
    val rating: Double,
    val photoSrc: String?,
    val biography: String?,
    val books: List<BookAuthor>?
)
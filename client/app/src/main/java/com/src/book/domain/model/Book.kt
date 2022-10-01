package com.src.book.domain.model

data class Book(
    val id: Long,
    val name: String,
    val rating: Double,
    val linkCover: String?,
    val year: String,
    val genres: List<Genre>?,
    val authors: List<AuthorBook>?,
    val reviews: List<Review>?,
    val description: String?,
    val tags: List<Tag>?
)
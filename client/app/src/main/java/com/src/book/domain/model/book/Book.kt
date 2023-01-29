package com.src.book.domain.model.book

import com.src.book.domain.author.AuthorBook
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Review
import com.src.book.domain.model.Tag

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
    val tags: List<Tag>?,
    val grade: Int?,
    val isWantToRead: Boolean
)
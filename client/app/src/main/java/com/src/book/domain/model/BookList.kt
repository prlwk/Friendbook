package com.src.book.domain.model

data class BookList(
    val id: Long,
    val name: String,
    val rating: Double,
    val linkCover: String?,
    val genres: List<Genre>?,
    val year: String,
    val authors: List<AuthorBook>?,
    val grade: Int?,
    var isWantToRead: Boolean,
    val isAuth:Boolean
)
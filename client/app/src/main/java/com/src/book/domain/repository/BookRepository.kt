package com.src.book.domain.repository

import com.src.book.domain.model.Book
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag

interface BookRepository {
    suspend fun getBooksByAuthorId(id: Long): List<Book>?
    suspend fun getBookById(id: Long): Book?
    suspend fun getAllTags(): List<Tag>?
    suspend fun getAllGenres(): List<Genre>?
}
package com.src.book.domain.repository

import com.src.book.domain.model.Book
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.utils.BasicState

interface BookRepository {
    suspend fun getBooksByAuthorId(id: Long): BasicState
    suspend fun getBookById(id: Long): Book?
    suspend fun getAllTags(): List<Tag>?
    suspend fun getAllGenres(): List<Genre>?
}
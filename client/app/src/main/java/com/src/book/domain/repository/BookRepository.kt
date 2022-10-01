package com.src.book.domain.repository

import com.src.book.domain.model.Book

interface BookRepository {
    suspend fun getBooksByAuthorId(id: Long): List<Book>?
    suspend fun getBookById(id: Long): Book?
}
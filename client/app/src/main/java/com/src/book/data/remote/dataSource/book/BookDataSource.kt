package com.src.book.data.remote.dataSource.book

import com.src.book.domain.model.Book

interface BookDataSource {
    suspend fun loadBooksByAuthorId(id: Long): List<Book>?
    suspend fun loadBookById(id: Long): Book?
}
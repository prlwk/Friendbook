package com.src.book.data.remote.dataSource.book

import com.src.book.domain.model.Book
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.utils.BasicState

interface BookDataSource {
    suspend fun loadBooksByAuthorId(id: Long): BasicState
    suspend fun loadBookById(id: Long): Book?
    suspend fun loadAllGenres(): List<Genre>?
    suspend fun loadAllTags(): List<Tag>?
}
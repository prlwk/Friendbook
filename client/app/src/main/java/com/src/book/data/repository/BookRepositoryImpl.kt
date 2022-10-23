package com.src.book.data.repository

import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.domain.model.Book
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepositoryImpl(private val bookDataSource: BookDataSource) : BookRepository {
    override suspend fun getBooksByAuthorId(id: Long): List<Book>? = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.loadBooksByAuthorId(id)
    }

    override suspend fun getBookById(id: Long): Book? = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.loadBookById(id)
    }

    override suspend fun getAllTags(): List<Tag>? = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.loadAllTags()
    }

    override suspend fun getAllGenres(): List<Genre>? = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.loadAllGenres()
    }
}
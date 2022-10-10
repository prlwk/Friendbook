package com.src.book.data.remote.dataSource.book

import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.service.BookService
import com.src.book.domain.model.*

class BookDataSourceImpl(
    private val bookService: BookService,
    private val bookMapper: BookMapper
) : BookDataSource {
    override suspend fun loadBooksByAuthorId(id: Long): List<Book>? {
        val booksResponse = bookService.getAllBooksByAuthorId(id)
        if (booksResponse.isSuccessful) {
            return booksResponse.body()?.map { bookMapper.mapFromResponseToModel(it) }
        }
        return null
    }

    override suspend fun loadBookById(id: Long): Book? {
        val bookResponse = bookService.getBookById(id)
        if (bookResponse.isSuccessful) {
            return bookResponse.body()?.let { bookMapper.mapFromResponseToModel(it) }
        }
        return null
    }
}
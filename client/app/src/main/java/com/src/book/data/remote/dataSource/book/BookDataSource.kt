package com.src.book.data.remote.dataSource.book

import com.src.book.domain.model.Book
import com.src.book.domain.model.BookList
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState

interface BookDataSource {
    suspend fun loadBooksByAuthorId(id: Long): BasicState<List<BookList>>
    suspend fun loadBookById(id: Long): Book?
    suspend fun loadAllGenres(): List<Genre>?
    suspend fun loadAllTags(): List<Tag>?
    suspend fun removeBookmark(bookId: Long): BookmarkState
    suspend fun addBookmark(bookId: Long): BookmarkState
}
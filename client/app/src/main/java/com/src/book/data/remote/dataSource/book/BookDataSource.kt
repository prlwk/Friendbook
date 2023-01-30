package com.src.book.data.remote.dataSource.book

import androidx.paging.PagingData
import com.src.book.data.remote.model.genre.GenreResponse
import com.src.book.domain.model.book.Book
import com.src.book.domain.model.book.BookList
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BookDataSource {
    suspend fun loadBooksByAuthorId(id: Long): BasicState<List<BookList>>
    suspend fun loadBookById(id: Long): Book?
    suspend fun loadAllGenres(): List<Genre>?
    suspend fun loadAllTags(): List<Tag>?
    suspend fun removeBookmark(bookId: Long): BookmarkState
    suspend fun addBookmark(bookId: Long): BookmarkState
    suspend fun searchBooks(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?
    ): BasicState<List<BookList>>

    fun searchBooksWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?
    ): Flow<PagingData<BookList>>

    suspend fun getPopularGenres(): BasicState<List<Genre>>
    suspend fun getPopularTags(): BasicState<List<Tag>>
}
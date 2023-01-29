package com.src.book.data.repository

import androidx.paging.PagingData
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.domain.model.book.Book
import com.src.book.domain.model.book.BookList
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BookRepositoryImpl(private val bookDataSource: BookDataSource) : BookRepository {
    override suspend fun getBooksByAuthorId(id: Long): BasicState<List<BookList>> =
        withContext(Dispatchers.IO) {
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

    override suspend fun addBookmark(bookId: Long): BookmarkState = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.addBookmark(bookId)
    }

    override suspend fun removeBookmark(bookId: Long): BookmarkState = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.removeBookmark(bookId)
    }

    override suspend fun searchBooks(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?
    ): BasicState<List<BookList>> = withContext(Dispatchers.IO) {
        return@withContext bookDataSource.searchBooks(
            numberPage = numberPage,
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating,
            tags = tags,
            genres = genres
        )
    }

    override  fun searchBooksWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?
    ): Flow<PagingData<BookList>> {
        return bookDataSource.searchBooksWithPagination(
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating,
            tags = tags,
            genres = genres
        )
    }
}
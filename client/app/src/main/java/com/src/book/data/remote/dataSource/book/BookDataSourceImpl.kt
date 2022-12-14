package com.src.book.data.remote.dataSource.book

import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.model.book.bookList.BookListMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.tag.TagMapper
import com.src.book.data.remote.model.token.RefreshTokenResponse
import com.src.book.data.remote.service.BookService
import com.src.book.data.remote.service.SessionService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.model.*
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState
import com.src.book.utils.TOKEN_TYPE

class BookDataSourceImpl(
    private val bookService: BookService,
    private val bookMapper: BookMapper,
    private val bookListMapper: BookListMapper,
    private val genreMapper: GenreMapper,
    private val tagMapper: TagMapper,
    private val sessionService: SessionService,
    private val sessionStorage: SessionStorage
) : BookDataSource {
    override suspend fun loadBooksByAuthorId(id: Long): BasicState {
        val token = getToken()
        val booksResponse = bookService.getAllBooksByAuthorId(id, token)
        if (booksResponse.isSuccessful) {
            val isAuth = token != null
            return BasicState.SuccessStateWithResources(
                booksResponse.body()
                    ?.map { bookListMapper.mapFromResponseToModel(it, isAuth = isAuth) })
        }
        return BasicState.ErrorState
    }

    override suspend fun loadBookById(id: Long): Book? {
        val bookResponse = bookService.getBookById(id)
        if (bookResponse.isSuccessful) {
            return bookResponse.body()?.let { bookMapper.mapFromResponseToModel(it) }
        }
        return null
    }

    override suspend fun loadAllGenres(): List<Genre>? {
        val genresResponse = bookService.getAllGenres()
        if (genresResponse.isSuccessful) {
            return genresResponse.body()?.map { genreMapper.mapFromResponseToModel(it) }
        }
        return null
    }

    override suspend fun loadAllTags(): List<Tag>? {
        val tagsResponse = bookService.getAllTags()
        if (tagsResponse.isSuccessful) {
            return tagsResponse.body()?.map { tagMapper.mapFromResponseToModel(it) }
        }
        return null
    }

    override suspend fun removeBookmark(bookId: Long): BookmarkState {
        val token = getToken()
        if (token == null) {
            return BookmarkState.NotAuthorizedState
        } else {
            val response = bookService.removeBookmark(token, bookId)
            if (response.isSuccessful) {
                return BookmarkState.SuccessState
            }
            return BookmarkState.ErrorState
        }
    }

    override suspend fun addBookmark(bookId: Long): BookmarkState {
        val token = getToken()
        if (token == null) {
            return BookmarkState.NotAuthorizedState
        } else {
            val response = bookService.addBookmark(token, bookId)
            if (response.isSuccessful) {
                return BookmarkState.SuccessState
            }
            return BookmarkState.ErrorState
        }
    }

    private fun getToken(): String? {
        if (sessionStorage.getAccessToken().isEmpty()) {
            return null
        } else {
            if (!sessionStorage.accessTokenIsValid()) {
                val needToRefreshToken = !sessionStorage.refreshTokenIsValid()
                val sessionResponse = sessionService.refreshTokens(
                    RefreshTokenResponse(
                        generateRefreshToken = needToRefreshToken,
                        email = sessionStorage.getEmail(),
                        accessToken = sessionStorage.getAccessToken(),
                        refreshToken = sessionStorage.getRefreshToken()
                    )
                ).execute().body()
                sessionResponse?.let {
                    sessionStorage.refreshAccessToken(
                        sessionResponse.accessToken,
                        it.expireTimeAccessToken
                    )
                    sessionResponse.refreshToken?.let { it1 ->
                        sessionResponse.expireTimeRefreshToken?.let { it2 ->
                            sessionStorage.refreshRefreshToken(
                                it1,
                                it2
                            )
                        }
                    }
                }
            }
            return "$TOKEN_TYPE ${sessionStorage.getAccessToken()}"
        }
    }
}
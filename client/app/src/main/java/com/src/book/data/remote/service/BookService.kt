package com.src.book.data.remote.service

import com.src.book.data.remote.model.book.book.BookResponse
import com.src.book.data.remote.model.book.bookList.BookListResponse
import com.src.book.data.remote.model.genre.GenreResponse
import com.src.book.data.remote.model.tag.TagResponse
import com.src.book.utils.BOOK_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BookService {
    @GET("${BOOK_SERVICE_BASE_URL}/book/by-author-id/{id}")
    suspend fun getAllBooksByAuthorId(
        @Path("id") id: Long,
        @Header("Authorization") token: String?
    ): Response<List<BookListResponse>>

    @GET("${BOOK_SERVICE_BASE_URL}/book/id/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookResponse>

    @GET("${BOOK_SERVICE_BASE_URL}/genre/all")
    suspend fun getAllGenres(): Response<List<GenreResponse>>

    @GET("${BOOK_SERVICE_BASE_URL}/tag/all")
    suspend fun getAllTags(): Response<List<TagResponse>>
}
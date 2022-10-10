package com.src.book.data.remote.service

import com.src.book.data.remote.model.book.book.BookResponse
import com.src.book.utlis.BOOK_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BookService {
    @GET("${BOOK_SERVICE_BASE_URL}book/by-author-id/{id}")
    suspend fun getAllBooksByAuthorId(@Path("id") id: Long): Response<List<BookResponse>>

    @GET("${BOOK_SERVICE_BASE_URL}book/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookResponse>
}
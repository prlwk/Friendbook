package com.src.book.data.remote.service

import android.net.Uri
import com.src.book.data.remote.model.book.BookResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface BookService {
    @GET
    suspend fun getAllBooksByAuthorId(@Url url: Uri): List<BookResponse>
}
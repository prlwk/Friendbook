package com.src.book.data.remote.service

import com.src.book.data.remote.model.author.author.AuthorResponse
import com.src.book.utlis.AUTHOR_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthorService {
    @GET("${AUTHOR_SERVICE_BASE_URL}author/{id}")
    suspend fun getAuthorById(@Path("id") id: Long): Response<AuthorResponse>
}
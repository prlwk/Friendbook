package com.src.book.data.remote.service

import android.net.Uri
import com.src.book.data.remote.model.author.AuthorResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface AuthorService {
    @GET
    suspend fun getAuthorById(@Url url: Uri): Response<AuthorResponse>
}
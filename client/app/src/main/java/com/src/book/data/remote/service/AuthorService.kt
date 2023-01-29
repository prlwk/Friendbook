package com.src.book.data.remote.service

import com.src.book.data.remote.model.author.author.AuthorResponse
import com.src.book.data.remote.model.author.authorList.AuthorListResponse
import com.src.book.data.remote.model.author.authorSearchResult.AuthorSearchResultResponse
import com.src.book.utils.AUTHOR_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthorService {
    @GET("${AUTHOR_SERVICE_BASE_URL}/author/{id}")
    suspend fun getAuthorById(@Path("id") id: Long): Response<AuthorResponse>

    @GET("$AUTHOR_SERVICE_BASE_URL/author/search")
    suspend fun searchAuthors(
        @Query("numberPage") numberPage: Int,
        @Query("sizePage") sizePage: Int,
        @Query("word") word: String?,
        @Query("sort") sort: String?,
        @Query("startRating") startRating: Int?,
        @Query("finishRating") finishRating: Int?
    ): Response<AuthorSearchResultResponse>
}
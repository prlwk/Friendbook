package com.src.book.data.remote.service

import com.src.book.utils.USER_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserServiceWithToken {
    @GET("$USER_SERVICE_BASE_URL/user/change-password")
    suspend fun changePassword(
        @Query("refreshToken", encoded = true) refreshToken: String,
        @Query("password", encoded = true) password: String
    ): Response<Unit>
}
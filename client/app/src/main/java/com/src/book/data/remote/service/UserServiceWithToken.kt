package com.src.book.data.remote.service

import com.src.book.utils.USER_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserServiceWithToken {
    @GET("$USER_SERVICE_BASE_URL/user/change-password")
    suspend fun changePassword(
        @Query("newPassword", encoded = true) newPassword: String,
        @Query("oldPassword", encoded = true) oldPassword: String,
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/user/logout")
    suspend fun logout(
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>

}
package com.src.book.data.remote.service

import com.src.book.data.remote.model.user.userProfile.UserProfileResponse
import com.src.book.utils.USER_SERVICE_BASE_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("$USER_SERVICE_BASE_URL/user/change-password")
    suspend fun changePassword(
        @Query("newPassword") newPassword: String,
        @Query("oldPassword") oldPassword: String?,
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/user/logout")
    suspend fun logout(
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>

    @Multipart
    @POST("$USER_SERVICE_BASE_URL/user/edit-profile")
    suspend fun editProfile(
        @Part("editUserBeanString") data: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>
}
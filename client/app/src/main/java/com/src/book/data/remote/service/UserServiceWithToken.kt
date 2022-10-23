package com.src.book.data.remote.service

import com.src.book.data.remote.model.user.changePassword.ChangePasswordResponse
import com.src.book.utils.USER_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserServiceWithToken {
    @POST("$USER_SERVICE_BASE_URL/user/change-password")
    suspend fun changePassword(@Body changePasswordResponse: ChangePasswordResponse): Response<Unit>
}
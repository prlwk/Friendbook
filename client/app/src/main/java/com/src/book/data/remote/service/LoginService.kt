package com.src.book.data.remote.service

import com.src.book.data.remote.model.login.loginAnswer.LoginAnswerResponse
import com.src.book.data.remote.model.user.login.LoginResponse
import com.src.book.utils.USER_SERVICE_BASE_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Accept: application/json")
    @POST("$USER_SERVICE_BASE_URL/user/login")
    suspend fun signIn(@Body data: LoginResponse): Response<LoginAnswerResponse>
}

package com.src.book.data.remote.service

import com.src.book.data.remote.model.login.emailExists.EmailExistsResponse
import com.src.book.data.remote.model.login.login.LoginResponse
import com.src.book.data.remote.model.login.loginAnswer.LoginAnswerResponse
import com.src.book.utils.USER_SERVICE_BASE_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface LoginService {
    @Headers("Accept: application/json")
    @POST("$USER_SERVICE_BASE_URL/user/login")
    suspend fun signIn(@Body data: LoginResponse): Response<LoginAnswerResponse>

    @GET("$USER_SERVICE_BASE_URL/user/check-email-exists")
    suspend fun checkEmailExists(
        @Query("email", encoded = true) email: String
    ): Response<EmailExistsResponse>

    @GET("$USER_SERVICE_BASE_URL/user/password-recovery")
    suspend fun checkRecoveryCode(
        @Query("code") code: String,
        @Query("email", encoded = true) email: String
    ): Response<LoginAnswerResponse>

    @GET("$USER_SERVICE_BASE_URL/send-code-for-recovery-password")
    suspend fun sendCodeForRecoveryPassword(
        @Query("email", encoded = true) email: String
    ): Response<Unit>

    @Multipart
    @POST("$USER_SERVICE_BASE_URL/user/registration")
    suspend fun signUp(
        @Part("registerBeanString") data: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<LoginAnswerResponse>

    @GET("$USER_SERVICE_BASE_URL/user/confirm-account")
    suspend fun checkRecoveryCodeForAccountConfirmations(
        @Query("confirmationCode") code: String,
        @Query("email", encoded = true) email: String
    ): Response<Unit>

    @GET("$USER_SERVICE_BASE_URL/send-code-for-confirmation-account")
    suspend fun sendCodeForAccountConfirmations(@Query("userId") userId: Long): Response<Unit>

}

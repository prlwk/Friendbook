package com.src.book.data.remote.service

import com.src.book.data.remote.model.token.TokenResponse
import com.src.book.data.remote.model.token.RefreshTokenResponse
import com.src.book.utils.USER_SERVICE_BASE_URL
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionService {
    @POST("$USER_SERVICE_BASE_URL/refresh-tokens")
    fun refreshTokens(
        @Body refreshTokenResponse: RefreshTokenResponse
    ): Call<TokenResponse>
}
package com.src.book.data.remote.utils

import com.src.book.data.remote.model.token.RefreshTokenResponse
import com.src.book.data.remote.service.SessionService
import com.src.book.data.remote.session.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class TokenInterceptor @Inject constructor(
    private val sessionServiceProvider: Provider<SessionService>,
    private val sessionStorageProvider: Provider<SessionStorage>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 403 || response.code == 401) {
            val sessionStorage = sessionStorageProvider.get()
            val sessionService = sessionServiceProvider.get()
            if (!sessionStorage.accessTokenIsValid()) {
                if (!sessionStorage.refreshTokenIsValid()) {
                    val refreshTokenResponse = RefreshTokenResponse(
                        generateRefreshToken = true,
                        email = sessionStorage.getEmail(),
                        accessToken = sessionStorage.getAccessToken(),
                        refreshToken = sessionStorage.getRefreshToken()
                    )
                    val tokenResponse = sessionService
                        .refreshTokens(refreshTokenResponse)
                        .execute()
                        .body()
                    tokenResponse?.let {
                        sessionStorage.refreshAccessToken(
                            tokenResponse.accessToken,
                            it.expireTimeAccessToken
                        )
                        tokenResponse.refreshToken?.let { it1 ->
                            tokenResponse.expireTimeRefreshToken?.let { it2 ->
                                sessionStorage.refreshRefreshToken(
                                    it1,
                                    it2
                                )
                            }
                        }
                    }
                } else {
                    val refreshTokenResponse = RefreshTokenResponse(
                        generateRefreshToken = false,
                        email = sessionStorage.getEmail(),
                        accessToken = sessionStorage.getAccessToken(),
                        refreshToken = sessionStorage.getRefreshToken()
                    )
                    val tokenResponse = sessionService
                        .refreshTokens(refreshTokenResponse)
                        .execute()
                        .body()
                    tokenResponse?.let {
                        sessionStorage.refreshAccessToken(
                            tokenResponse.accessToken,
                            it.expireTimeAccessToken
                        )
                    }
                }
            }
            response.close()
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${sessionStorage.getAccessToken()}")
                .build()
            return chain.proceed(newRequest)
        }
        response.close()
        val newRequest = chain.request().newBuilder()
            .build()
        return chain.proceed(newRequest)
    }
}
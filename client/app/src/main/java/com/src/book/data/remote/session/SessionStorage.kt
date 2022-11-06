package com.src.book.data.remote.session

interface SessionStorage {
    fun refresh(
        refreshToken: String,
        accessToken: String,
        expireTimeRefreshToken: String,
        expireTimeAccessToken: String,
        id: String,
        email: String
    )

    fun getRefreshToken(): String
    fun getTokensAndEmail(): Map<String, String>
    fun getAccessToken(): String
    fun accessTokenIsValid(): Boolean
    fun refreshTokenIsValid(): Boolean
    fun refreshAccessToken(
        accessToken: String,
        expireTimeAccessToken: String
    )

    fun refreshRefreshToken(
        refreshToken: String,
        expireTimeRefreshToken: String
    )

    fun getEmail(): String
    fun clearSession()
    fun getId():String
}
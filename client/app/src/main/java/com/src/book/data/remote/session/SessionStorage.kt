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
}
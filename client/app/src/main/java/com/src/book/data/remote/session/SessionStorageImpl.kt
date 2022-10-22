package com.src.book.data.remote.session

import android.content.Context
import com.src.book.utlis.*

class SessionStorageImpl(context: Context) : SessionStorage {
    private val sharedPreferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
    override fun refresh(
        refreshToken: String,
        accessToken: String,
        expireTimeRefreshToken: String,
        expireTimeAccessToken: String,
        id: String,
        email: String
    ) {
        sharedPreferences.edit()
            .putString(REFRESH_TOKEN, refreshToken)
            .putString(ACCESS_TOKEN, accessToken)
            .putString(CURRENT_DATE, System.currentTimeMillis().toString())
            .putString(EXPIRE_TIME_REFRESH_TOKEN, expireTimeRefreshToken)
            .putString(EXPIRE_TIME_ACCESS_TOKEN, expireTimeAccessToken)
            .putString(ID, id)
            .putString(EMAIL, email)
            .apply()
    }

    override fun getRefreshToken(): String {
        return sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""
    }
}
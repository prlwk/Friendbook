package com.src.book.data.remote.session

import android.content.Context

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
            .putString(CURRENT_DATE_ACCESS_TOKEN, System.currentTimeMillis().toString())
            .putString(CURRENT_DATE_REFRESH_TOKEN, System.currentTimeMillis().toString())
            .putString(EXPIRE_TIME_REFRESH_TOKEN, expireTimeRefreshToken)
            .putString(EXPIRE_TIME_ACCESS_TOKEN, expireTimeAccessToken)
            .putString(ID, id)
            .putString(EMAIL, email)
            .apply()
    }

    override fun getRefreshToken(): String {
        return sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""
    }

    override fun getTokensAndEmail(): Map<String, String> {
        return mapOf(
            REFRESH_TOKEN to (sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""),
            ACCESS_TOKEN to (sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""),
            EMAIL to (sharedPreferences.getString(EMAIL, "") ?: "")
        )
    }

    override fun getAccessToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    override fun accessTokenIsValid(): Boolean {
        val currentDate =
            (sharedPreferences.getString(CURRENT_DATE_ACCESS_TOKEN, "") ?: "").toLong()
        val expireTime =
            (sharedPreferences.getString(EXPIRE_TIME_ACCESS_TOKEN, "") ?: "").toLong()
        return currentDate + expireTime >= System.currentTimeMillis()
    }

    override fun refreshTokenIsValid(): Boolean {
        val currentDate =
            (sharedPreferences.getString(CURRENT_DATE_REFRESH_TOKEN, "") ?: "").toLong()
        val expireTime =
            (sharedPreferences.getString(EXPIRE_TIME_REFRESH_TOKEN, "") ?: "").toLong()
        return currentDate + expireTime >= System.currentTimeMillis()
    }

    override fun refreshAccessToken(accessToken: String, expireTimeAccessToken: String) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(CURRENT_DATE_ACCESS_TOKEN, System.currentTimeMillis().toString())
            .putString(EXPIRE_TIME_ACCESS_TOKEN, expireTimeAccessToken)
            .apply()
    }

    override fun refreshRefreshToken(refreshToken: String, expireTimeRefreshToken: String) {
        sharedPreferences.edit()
            .putString(REFRESH_TOKEN, refreshToken)
            .putString(CURRENT_DATE_REFRESH_TOKEN, System.currentTimeMillis().toString())
            .putString(EXPIRE_TIME_REFRESH_TOKEN, expireTimeRefreshToken)
            .apply()
    }

    override fun getEmail(): String {
        return (sharedPreferences.getString(EMAIL, "") ?: "")
    }

    override fun clearSession() {
        sharedPreferences.edit()
            .putString(REFRESH_TOKEN, "")
            .putString(ACCESS_TOKEN, "")
            .putString(CURRENT_DATE_ACCESS_TOKEN, "")
            .putString(CURRENT_DATE_REFRESH_TOKEN, "")
            .putString(EXPIRE_TIME_REFRESH_TOKEN, "")
            .putString(EXPIRE_TIME_ACCESS_TOKEN, "")
            .putString(ID, "")
            .putString(EMAIL, "")
            .putBoolean(IS_ACTIVE, false)
            .apply()
    }

    override fun getId(): String {
        return (sharedPreferences.getString(ID, "") ?: "")
    }

    override fun setIsActive(isActive: Boolean) {
        sharedPreferences.edit().putBoolean(IS_ACTIVE, isActive).apply()
    }

    override fun getIsActive(): Boolean {
        return sharedPreferences.getBoolean(IS_ACTIVE, false)
    }

    companion object {
        const val REFRESH_TOKEN = "refreshToken"
        const val ACCESS_TOKEN = "accessToken"
        const val EXPIRE_TIME_REFRESH_TOKEN = "expireTimeRefreshToken"
        const val EXPIRE_TIME_ACCESS_TOKEN = "expireTimeAccessToken"
        const val ID = "id"
        const val EMAIL = "email"
        const val USER_DATA = "user_data"
        const val CURRENT_DATE_ACCESS_TOKEN = "current_date_access_token"
        const val CURRENT_DATE_REFRESH_TOKEN = "current_date_refresh_token"
        const val TOKEN_TYPE = "Bearer"
        const val IS_ACTIVE = "isActive"
    }
}
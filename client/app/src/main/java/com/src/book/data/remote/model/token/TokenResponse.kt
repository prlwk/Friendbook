package com.src.book.data.remote.model.token

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class TokenResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("refresh-token")
    val refreshToken: String?,
    @SerializedName("expireTimeRefreshToken")
    val expireTimeRefreshToken: String?,
    @SerializedName("expireTimeAccessToken")
    val expireTimeAccessToken: String,
    @SerializedName("access-token")
    val accessToken: String,
    )
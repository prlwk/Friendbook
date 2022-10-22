package com.src.book.data.remote.model.token

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class RefreshTokenResponse(
    @SerializedName("generateRefreshToken")
    val generateRefreshToken: Boolean,
    @SerializedName("email")
    val email: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("accessToken")
    val accessToken: String
)
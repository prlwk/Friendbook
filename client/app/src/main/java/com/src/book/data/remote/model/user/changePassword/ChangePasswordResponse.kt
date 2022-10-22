package com.src.book.data.remote.model.user.changePassword

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class ChangePasswordResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
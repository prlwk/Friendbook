package com.src.book.data.remote.model.user.login

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class LoginResponse (
    @SerializedName("loginOrEmail")
    val loginOrEmail:String,
    @SerializedName("password")
    val password:String,
    @SerializedName("isEntryByEmail")
    val isEntryByEmail:String
    )
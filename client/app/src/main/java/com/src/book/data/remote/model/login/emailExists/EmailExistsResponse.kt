package com.src.book.data.remote.model.login.emailExists

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class EmailExistsResponse(
    @SerializedName("exists") val exists: Boolean
)
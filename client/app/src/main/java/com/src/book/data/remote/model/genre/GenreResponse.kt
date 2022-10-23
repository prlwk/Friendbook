package com.src.book.data.remote.model.genre

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class GenreResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
)
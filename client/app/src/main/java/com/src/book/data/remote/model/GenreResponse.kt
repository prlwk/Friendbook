package com.src.book.data.remote.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class GenreResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
)
package com.src.book.data.remote.model.author

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class AuthorBookResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
)
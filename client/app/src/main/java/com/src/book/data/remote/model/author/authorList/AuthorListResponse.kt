package com.src.book.data.remote.model.author.authorList

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class AuthorListResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("photoSrc") val photoSrc: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("yearsLife") val yearsLife: String?
)
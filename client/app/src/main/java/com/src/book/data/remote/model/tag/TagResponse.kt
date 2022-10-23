package com.src.book.data.remote.model.tag

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class TagResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
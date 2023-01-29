package com.src.book.data.remote.model.author.authorSearchResult

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.author.authorList.AuthorListResponse

@kotlinx.serialization.Serializable
class AuthorSearchResultResponse(
    @SerializedName("content") val listOfAuthors: List<AuthorListResponse>,
    @SerializedName("totalPages") val totalPages: Int
)
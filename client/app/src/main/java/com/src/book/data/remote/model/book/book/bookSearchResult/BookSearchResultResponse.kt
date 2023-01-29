package com.src.book.data.remote.model.book.book.bookSearchResult

import com.google.gson.annotations.SerializedName
import com.src.book.data.remote.model.book.bookList.BookListResponse

@kotlinx.serialization.Serializable
class BookSearchResultResponse(
    @SerializedName("content")
    val listOfBooks: List<BookListResponse>,
    @SerializedName("totalPages")
    val totalPages: Int
)
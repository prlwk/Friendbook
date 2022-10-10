package com.src.book.data.remote.model.book.bookAuthor

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.BookAuthor

class BookAuthorMapper : Mapper<BookAuthor, BookAuthorResponse> {
    override suspend fun mapFromResponseToModel(data: BookAuthorResponse): BookAuthor {
        return BookAuthor(
            id = data.id,
            rating = data.rating,
            linkCover = data.linkCover
        )
    }
}
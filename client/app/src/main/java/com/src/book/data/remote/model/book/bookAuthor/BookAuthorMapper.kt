package com.src.book.data.remote.model.book.bookAuthor

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.book.BookAuthor
import com.src.book.utils.BASE_URL
import com.src.book.utils.BOOK_SERVICE_BASE_URL

class BookAuthorMapper : Mapper<BookAuthor, BookAuthorResponse> {
    override suspend fun mapFromResponseToModel(data: BookAuthorResponse): BookAuthor {
        return BookAuthor(
            id = data.id,
            rating = data.rating,
            linkCover = "$BASE_URL$BOOK_SERVICE_BASE_URL${data.linkCover}"
        )
    }
}
package com.src.book.data.remote.model.author.author

import com.src.book.data.remote.Mapper
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorMapper
import com.src.book.domain.model.Author
import com.src.book.utils.AUTHOR_SERVICE_BASE_URL
import com.src.book.utils.BASE_URL

class AuthorMapper(private val bookAuthorMapper: BookAuthorMapper) :
    Mapper<Author, AuthorResponse> {

    override suspend fun mapFromResponseToModel(data: AuthorResponse): Author {
        return Author(
            id = data.id,
            name = data.name,
            yearsLife = data.yearsLife,
            rating = data.rating,
            photoSrc = "$BASE_URL$AUTHOR_SERVICE_BASE_URL${data.photoSrc}",
            biography = data.biography,
            books = data.books?.map {
                bookAuthorMapper.mapFromResponseToModel(it)
            }
        )
    }
}
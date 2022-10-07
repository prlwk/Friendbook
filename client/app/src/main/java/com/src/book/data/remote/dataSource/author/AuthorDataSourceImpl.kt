package com.src.book.data.remote.dataSource.author

import com.src.book.data.remote.model.author.AuthorResponse
import com.src.book.data.remote.model.book.BookAuthorResponse
import com.src.book.data.remote.service.AuthorService
import com.src.book.domain.model.Author
import com.src.book.domain.model.BookAuthor

class AuthorDataSourceImpl(private val authorService: AuthorService) : AuthorDataSource {
    override suspend fun loadAuthorById(id: Long): Author? {
        return try {
            val authorResponse = authorService.getAuthorById(id)
            authorResponse.body()?.let { responseAuthorToModel(it) }
        } catch (exception: Exception) {
            null
        }
    }

    private suspend fun responseAuthorToModel(authorResponse: AuthorResponse): Author {
        return Author(
            id = authorResponse.id,
            name = authorResponse.name,
            yearsLife = authorResponse.yearsLife,
            rating = authorResponse.rating,
            photoSrc = authorResponse.photoSrc,
            biography = authorResponse.biography,
            books = authorResponse.books?.map { bookAuthorResponse ->
                responseBookAuthorToModel(
                    bookAuthorResponse
                )
            }
        )
    }

    private suspend fun responseBookAuthorToModel(bookAuthorResponse: BookAuthorResponse) =
        BookAuthor(
            id = bookAuthorResponse.id,
            rating = bookAuthorResponse.rating,
            linkCover = bookAuthorResponse.linkCover
        )
}
package com.src.book.data.remote.model.author.authorBook

import com.src.book.data.remote.Mapper
import com.src.book.domain.author.AuthorBook

class AuthorBookMapper : Mapper<AuthorBook, AuthorBookResponse> {
    override suspend fun mapFromResponseToModel(data: AuthorBookResponse): AuthorBook {
        return AuthorBook(
            id = data.id,
            name = data.name
        )
    }
}
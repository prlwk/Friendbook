package com.src.book.data.remote.model.author.authorList

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.author.AuthorList

class AuthorListMapper : Mapper<AuthorList, AuthorListResponse> {
    override suspend fun mapFromResponseToModel(data: AuthorListResponse) = AuthorList(
        id = data.id,
        name = data.name,
        photoSrc = data.photoSrc,
        yearsLife = data.yearsLife,
        rating = data.rating
    )
}
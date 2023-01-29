package com.src.book.data.remote.model.author.authorList

import com.src.book.data.remote.Mapper
import com.src.book.domain.author.AuthorList
import com.src.book.utils.AUTHOR_SERVICE_BASE_URL
import com.src.book.utils.BASE_URL

class AuthorListMapper : Mapper<AuthorList, AuthorListResponse> {
    override suspend fun mapFromResponseToModel(data: AuthorListResponse) = AuthorList(
        id = data.id,
        name = data.name,
        photoSrc = "$BASE_URL$AUTHOR_SERVICE_BASE_URL${data.photoSrc}",
        yearsLife = data.yearsLife,
        rating = data.rating
    )
}
package com.src.book.data.remote.dataSource.author

import com.src.book.data.remote.model.author.author.AuthorMapper
import com.src.book.data.remote.service.AuthorService
import com.src.book.domain.model.Author

class AuthorDataSourceImpl(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper
) : AuthorDataSource {
    override suspend fun loadAuthorById(id: Long): Author? {
        val authorResponse = authorService.getAuthorById(id)
        if (authorResponse.isSuccessful) {
            return authorResponse.body()?.let { authorMapper.mapFromResponseToModel(it) }
        }
        return null
    }
}
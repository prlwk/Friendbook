package com.src.book.data.repository

import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.domain.model.Author
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorRepositoryImpl(private val authorDataSource: AuthorDataSource) : AuthorRepository {
    override suspend fun getAuthorById(id: Long): BasicState<Author> =
        withContext(Dispatchers.IO) {
            return@withContext authorDataSource.loadAuthorById(id)
        }
}
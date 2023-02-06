package com.src.book.data.repository

import androidx.paging.PagingData
import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.domain.author.Author
import com.src.book.domain.author.AuthorList
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthorRepositoryImpl(private val authorDataSource: AuthorDataSource) : AuthorRepository {
    override suspend fun getAuthorById(id: Long): BasicState<Author> =
        withContext(Dispatchers.IO) {
            return@withContext authorDataSource.loadAuthorById(id)
        }

    override suspend fun searchAuthors(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): BasicState<List<AuthorList>> = withContext(Dispatchers.IO) {
        return@withContext authorDataSource.searchAuthors(
            numberPage = numberPage,
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
    }

    override fun searchAuthorsWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>> {
        return authorDataSource.searchAuthorsWithPagination(
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
    }

    override fun searchTopAuthorsWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>> {
        return authorDataSource.searchTopAuthorsWithPagination(
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
    }
}
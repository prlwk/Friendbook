package com.src.book.data.remote.dataSource.author

import androidx.paging.PagingData
import com.src.book.domain.model.author.Author
import com.src.book.domain.model.author.AuthorList
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.flow.Flow

interface AuthorDataSource {
    suspend fun loadAuthorById(id: Long): BasicState<Author>
    suspend fun searchAuthors(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): BasicState<List<AuthorList>>

    fun searchAuthorsWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>>

    fun searchTopAuthorsWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>>
}
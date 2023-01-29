package com.src.book.data.remote.dataSource.author

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.src.book.data.paging.author.AuthorPagingSource
import com.src.book.data.remote.model.author.author.AuthorMapper
import com.src.book.data.remote.model.author.authorList.AuthorListMapper
import com.src.book.data.remote.service.AuthorService
import com.src.book.domain.author.Author
import com.src.book.domain.author.AuthorList
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.flow.Flow

class AuthorDataSourceImpl(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper,
    private val authorListMapper: AuthorListMapper
) : AuthorDataSource {
    override suspend fun loadAuthorById(id: Long): BasicState<Author> {
        val authorResponse = authorService.getAuthorById(id)
        if (authorResponse.isSuccessful) {
            val author =
                authorResponse.body()?.let { authorMapper.mapFromResponseToModel(it) }
            if (author != null) {
                return BasicState.SuccessState(author)
            }
        }
        return BasicState.ErrorState()
    }

    override suspend fun searchAuthors(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): BasicState<List<AuthorList>> {
        val response = authorService.searchAuthors(
            numberPage = numberPage,
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
        if (response.isSuccessful) {
            if (response.body() != null) {
                val authors =
                    response.body()?.listOfAuthors?.map { authorListMapper.mapFromResponseToModel(it) }
                if (authors != null) {
                    return BasicState.SuccessState(authors)
                }
            }
        }
        if (response.code() == 404) {
            return BasicState.EmptyState()
        }
        return BasicState.ErrorState()
    }

    override fun searchAuthorsWithPagination(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>> {
        return Pager(
            config = PagingConfig(
                pageSize = sizePage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AuthorPagingSource(
                    authorService = authorService,
                    sizePage = sizePage,
                    word = word,
                    sort = sort,
                    startRating = startRating,
                    finishRating = finishRating
                )
            }
        ).flow
    }
}
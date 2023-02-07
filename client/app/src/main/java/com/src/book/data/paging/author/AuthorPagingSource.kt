package com.src.book.data.paging.author

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.src.book.data.paging.ErrorLoadingException
import com.src.book.data.remote.model.author.authorList.AuthorListMapper
import com.src.book.data.remote.service.AuthorService
import com.src.book.domain.model.author.AuthorList
import java.io.IOException

class AuthorPagingSource(
    private val authorService: AuthorService,
    private val sizePage: Int,
    private val word: String?,
    private val sort: String?,
    private val startRating: Int?,
    private val finishRating: Int?
) :
    PagingSource<Int, AuthorList>() {
    override fun getRefreshKey(state: PagingState<Int, AuthorList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AuthorList> {
        try {
            val nextPageNumber = params.key ?: 0
            val response = authorService.searchAuthors(
                numberPage = nextPageNumber,
                sizePage = sizePage,
                word = word,
                sort = sort,
                startRating = startRating,
                finishRating = finishRating
            )
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val result: List<AuthorList>? = response.body()?.listOfAuthors?.map {
                        AuthorListMapper().mapFromResponseToModel(it)
                    }
                    if (result != null) {
                        return LoadResult.Page(
                            data = result,
                            prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                            nextKey = if (nextPageNumber < response.body()?.totalPages!!) nextPageNumber + 1 else null
                        )
                    }
                }
            }
            if (response.code() == 404) {
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                    nextKey = if (nextPageNumber < response.body()?.totalPages!!) nextPageNumber + 1 else null
                )
            }
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
        return LoadResult.Error(ErrorLoadingException("Error loading Authors"))
    }
}
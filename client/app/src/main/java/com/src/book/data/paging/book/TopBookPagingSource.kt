package com.src.book.data.paging.book

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.src.book.data.paging.ErrorLoadingException
import com.src.book.data.remote.model.book.bookList.BookListMapper
import com.src.book.data.remote.service.BookService
import com.src.book.domain.model.book.BookList
import java.io.IOException

class TopBookPagingSource(
    private val bookService: BookService,
    private val sizePage: Int,
    private val token: String?,
    private val word: String?,
    private val sort: String?,
    private val startRating: Int?,
    private val finishRating: Int?,
    private val tags: String?,
    private val genres: String?,
    private val bookListMapper: BookListMapper
) : PagingSource<Int, BookList>() {
    override fun getRefreshKey(state: PagingState<Int, BookList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookList> {
        try {
            val nextPageNumber = params.key ?: 0
            val response = bookService.searchBooks(
                token = token,
                numberPage = nextPageNumber,
                sizePage = sizePage,
                word = word,
                sort = sort,
                startRating = startRating,
                finishRating = finishRating,
                tags = tags,
                genres = genres
            )
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val isAuth = token != null
                    val result = response.body()?.listOfBooks?.map {
                        bookListMapper.mapFromResponseToModel(it, isAuth)
                    }
                    if (result != null) {
                        val nextKey =
                            if ((nextPageNumber >= response.body()?.totalPages!!)
                                || ((nextPageNumber + 1) * sizePage >= MAX_COUNT_OF_BOOKS)
                            ) null else nextPageNumber + 1

                        return LoadResult.Page(
                            data = result,
                            prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                            nextKey = nextKey
                        )
                    }
                }
            }
            if (response.code() == 404) {
                return LoadResult.Page(
                    data = listOf(),
                    prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                    nextKey = null
                )
            }

        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
        return LoadResult.Error(ErrorLoadingException("Error loading Books"))
    }

    private companion object {
        private const val MAX_COUNT_OF_BOOKS = 100
    }
}
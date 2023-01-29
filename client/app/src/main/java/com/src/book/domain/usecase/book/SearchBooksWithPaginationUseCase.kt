package com.src.book.domain.usecase.book

import androidx.paging.PagingData
import com.src.book.domain.model.book.BookList
import com.src.book.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class SearchBooksWithPaginationUseCase(private val bookRepository: BookRepository) {
    fun execute(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?
    ): Flow<PagingData<BookList>> {
        return bookRepository.searchBooksWithPagination(
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating,
            tags = tags,
            genres = genres
        )
    }
}
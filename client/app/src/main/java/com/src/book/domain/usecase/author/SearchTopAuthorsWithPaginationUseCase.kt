package com.src.book.domain.usecase.author

import androidx.paging.PagingData
import com.src.book.domain.model.author.AuthorList
import com.src.book.domain.repository.AuthorRepository
import kotlinx.coroutines.flow.Flow

class SearchTopAuthorsWithPaginationUseCase(private val authorRepository: AuthorRepository) {
    fun execute(
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>> {
        return authorRepository.searchTopAuthorsWithPagination(
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
    }
}
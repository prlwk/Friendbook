package com.src.book.domain.usecase.author

import com.src.book.domain.author.AuthorList
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchAuthorsUseCase(private val authorRepository: AuthorRepository) {
    suspend fun execute(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): BasicState<List<AuthorList>> = withContext(Dispatchers.IO) {
        return@withContext authorRepository.searchAuthors(
            numberPage = numberPage,
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
    }
}
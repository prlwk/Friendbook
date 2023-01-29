package com.src.book.domain.usecase.book

import com.src.book.domain.model.book.BookList
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchBooksUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(
        numberPage: Int,
        sizePage: Int,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: List<Int>?,
        genres: List<Int>?
    ): BasicState<List<BookList>> = withContext(Dispatchers.IO) {
        return@withContext bookRepository.searchBooks(
            numberPage = numberPage,
            sizePage = sizePage,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating,
            tags = tags?.joinToString(separator = ","),
            genres = genres?.joinToString(separator = ",")
        )
    }
}
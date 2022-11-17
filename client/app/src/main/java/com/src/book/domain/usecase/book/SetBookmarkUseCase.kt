package com.src.book.domain.usecase.book

import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BookmarkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetBookmarkUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(bookId: Long, isSaving: Boolean): BookmarkState =
        withContext(Dispatchers.IO) {
            if (isSaving) {
                return@withContext bookRepository.removeBookmark(bookId)
            } else {
                return@withContext bookRepository.addBookmark(bookId)
            }
        }
}
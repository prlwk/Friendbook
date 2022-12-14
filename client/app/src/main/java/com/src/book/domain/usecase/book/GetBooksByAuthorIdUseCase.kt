package com.src.book.domain.usecase.book

import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBooksByAuthorIdUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(id: Long): BasicState = withContext(Dispatchers.IO) {
        return@withContext bookRepository.getBooksByAuthorId(id)
    }
}
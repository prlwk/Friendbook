package com.src.book.domain.usecase

import com.src.book.domain.model.Book
import com.src.book.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBooksByAuthorIdUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(id: Long): List<Book>? = withContext(Dispatchers.IO) {
        return@withContext bookRepository.getBooksByAuthorId(id)
    }
}
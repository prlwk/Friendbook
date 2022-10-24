package com.src.book.domain.usecase.book

import com.src.book.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBookByIdUseCase(private val bookRepository: BookRepository) {

    suspend fun execute(id: Long) = withContext(Dispatchers.IO) {
        return@withContext bookRepository.getBookById(id)
    }

}
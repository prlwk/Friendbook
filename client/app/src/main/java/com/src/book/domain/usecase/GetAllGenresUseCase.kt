package com.src.book.domain.usecase

import com.src.book.domain.model.Genre
import com.src.book.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllGenresUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(): List<Genre>? = withContext(Dispatchers.IO) {
        return@withContext bookRepository.getAllGenres()
    }
}
package com.src.book.domain.usecase

import com.src.book.domain.model.Author
import com.src.book.domain.repository.AuthorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAuthorUseCase(private val authorRepository: AuthorRepository) {
    suspend fun execute(id: Long): Author? = withContext(Dispatchers.IO) {
        return@withContext authorRepository.getAuthorById(id)
    }
}
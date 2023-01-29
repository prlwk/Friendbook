package com.src.book.domain.usecase.author

import com.src.book.domain.author.Author
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAuthorUseCase(private val authorRepository: AuthorRepository) {
    suspend fun execute(id: Long): BasicState<Author> = withContext(Dispatchers.IO) {
        return@withContext authorRepository.getAuthorById(id)
    }
}
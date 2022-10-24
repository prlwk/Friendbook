package com.src.book.domain.usecase.search

import com.src.book.domain.model.Tag
import com.src.book.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllTagsUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(): List<Tag>? = withContext(Dispatchers.IO) {
        return@withContext bookRepository.getAllTags()
    }
}
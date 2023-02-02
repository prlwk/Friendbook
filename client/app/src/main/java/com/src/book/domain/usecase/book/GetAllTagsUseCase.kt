package com.src.book.domain.usecase.book

import com.src.book.domain.model.Tag
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllTagsUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(): BasicState<List<Tag>> = withContext(Dispatchers.IO) {
        return@withContext bookRepository.getAllTags()
    }
}
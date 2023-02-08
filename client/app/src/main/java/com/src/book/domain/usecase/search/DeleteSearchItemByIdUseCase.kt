package com.src.book.domain.usecase.search

import com.src.book.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSearchItemByIdUseCase(private val searchRepository: SearchRepository) {
    suspend fun execute(id: Long) = withContext(Dispatchers.IO) {
        searchRepository.deleteById(id = id)
    }
}
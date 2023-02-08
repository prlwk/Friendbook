package com.src.book.domain.usecase.search

import com.src.book.domain.model.search.SearchItem
import com.src.book.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddSearchItemUseCase(private val searchRepository: SearchRepository) {
    suspend fun execute(name: String) = withContext(Dispatchers.IO) {
        searchRepository.addSearchItem(searchItem = SearchItem(id = 0, name = name))
    }
}
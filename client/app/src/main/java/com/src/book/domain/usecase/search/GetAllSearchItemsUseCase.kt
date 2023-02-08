package com.src.book.domain.usecase.search

import com.src.book.domain.model.search.SearchItem
import com.src.book.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class GetAllSearchItemsUseCase(private val searchRepository: SearchRepository) {
    fun execute(): Flow<List<SearchItem>> {
        return searchRepository.getAllSearchItems()
    }
}
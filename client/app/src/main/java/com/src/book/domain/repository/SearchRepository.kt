package com.src.book.domain.repository

import com.src.book.domain.model.search.SearchItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun deleteById(id: Long)
    fun getAllSearchItems(): Flow<List<SearchItem>>
    suspend fun addSearchItem(searchItem: SearchItem)
}
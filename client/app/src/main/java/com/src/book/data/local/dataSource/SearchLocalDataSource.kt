package com.src.book.data.local.dataSource

import com.src.book.domain.model.search.SearchItem
import kotlinx.coroutines.flow.Flow

interface SearchLocalDataSource {
    suspend fun deleteById(id: Long)
    fun getAllSearchItems(): Flow<List<SearchItem>>
    suspend fun addSearchItem(searchItem: SearchItem)
}
package com.src.book.data.repository

import com.src.book.data.local.dataSource.SearchLocalDataSource
import com.src.book.domain.model.search.SearchItem
import com.src.book.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(private val searchLocalDataSource: SearchLocalDataSource) :
    SearchRepository {
    override suspend fun deleteById(id: Long) {
        searchLocalDataSource.deleteById(id = id)
    }

    override fun getAllSearchItems(): Flow<List<SearchItem>> {
        return searchLocalDataSource.getAllSearchItems()

    }

    override suspend fun addSearchItem(searchItem: SearchItem) {
        searchLocalDataSource.addSearchItem(searchItem)
    }
}
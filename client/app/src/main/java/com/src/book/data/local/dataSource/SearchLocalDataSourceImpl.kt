package com.src.book.data.local.dataSource

import com.src.book.data.local.database.AppRoomDatabase
import com.src.book.data.local.database.entity.SearchItemEntity
import com.src.book.domain.model.search.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SearchLocalDataSourceImpl(private val database: AppRoomDatabase) : SearchLocalDataSource {
    override suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        database.getSearchItemDao().deleteById(id = id)
    }

    override fun getAllSearchItems(): Flow<List<SearchItem>> {
        return database.getSearchItemDao().getAllSearchItems().map { list ->
            list.map {
                SearchItemEntity.toModel(it)
            }
        }
    }

    override suspend fun addSearchItem(searchItem: SearchItem) {
        val count = database.getSearchItemDao().getCountOfItems()
        if (count >= 10L) {
            database.getSearchItemDao().deleteTheOldestItem()
        }
        database.getSearchItemDao()
            .insertSearchItem(taskEntity = SearchItemEntity.fromModel(searchItem))
    }
}
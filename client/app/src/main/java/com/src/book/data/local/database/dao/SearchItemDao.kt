package com.src.book.data.local.database.dao

import androidx.room.*
import com.src.book.data.local.database.entity.SearchItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchItemDao {
    @Query(
        "select * from search_items" +
                " order by id desc"
    )
    fun getAllSearchItems(): Flow<List<SearchItemEntity>>

    @Query("delete from search_items where id=:id")
    fun deleteById(id: Long)

    @Query("select count(search_items.id) as count from search_items")
    fun getCountOfItems(): Long

    @Query(
        "delete  from search_items " +
                "where id = (select id from search_items order by id asc limit 1)"
    )
    fun deleteTheOldestItem()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchItem(taskEntity: SearchItemEntity)
}
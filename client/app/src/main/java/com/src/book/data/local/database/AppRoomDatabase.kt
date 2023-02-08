package com.src.book.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.src.book.data.local.database.dao.SearchItemDao
import com.src.book.data.local.database.entity.SearchItemEntity

@Database(
    entities = [SearchItemEntity::class],
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "search.db"
    }

    abstract fun getSearchItemDao(): SearchItemDao
}
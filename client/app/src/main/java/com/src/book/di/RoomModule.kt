package com.src.book.di

import android.content.Context
import androidx.room.Room
import com.src.book.data.local.dataSource.SearchLocalDataSource
import com.src.book.data.local.dataSource.SearchLocalDataSourceImpl
import com.src.book.data.local.database.AppRoomDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @Provides
    fun provideAppRoomDatabase(context: Context): AppRoomDatabase {
        return Room.databaseBuilder(
            context,
            AppRoomDatabase::class.java,
            AppRoomDatabase.DATABASE_NAME
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideLocalDataSource(db: AppRoomDatabase): SearchLocalDataSource {
        return SearchLocalDataSourceImpl(db)
    }

}
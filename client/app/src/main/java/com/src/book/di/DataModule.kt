package com.src.book.di

import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.repository.AuthorRepositoryImpl
import com.src.book.domain.repository.AuthorRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    fun provideAuthorRepository(authorDataSource: AuthorDataSource): AuthorRepository {
        return AuthorRepositoryImpl(authorDataSource)
    }
}
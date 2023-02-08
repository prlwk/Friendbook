package com.src.book.di

import com.src.book.data.local.repository.LocalUserRepository
import com.src.book.data.local.repository.LocalUserRepositoryImpl
import com.src.book.data.remote.session.SessionStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {
    @Singleton
    @Provides
    fun provideLocalUserRepository(sessionStorage: SessionStorage): LocalUserRepository {
        return LocalUserRepositoryImpl(sessionStorage = sessionStorage)
    }
}
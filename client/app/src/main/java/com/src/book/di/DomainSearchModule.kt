package com.src.book.di

import com.src.book.domain.repository.SearchRepository
import com.src.book.domain.usecase.search.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainSearchModule {
    @Singleton
    @Provides
    fun provideAddSearchItemBUseCase(searchRepository: SearchRepository): AddSearchItemUseCase {
        return AddSearchItemUseCase(searchRepository = searchRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSearchItemByIdUseCase(searchRepository: SearchRepository): DeleteSearchItemByIdUseCase {
        return DeleteSearchItemByIdUseCase(searchRepository = searchRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllSearchItemsUseCase(searchRepository: SearchRepository): GetAllSearchItemsUseCase {
        return GetAllSearchItemsUseCase(searchRepository = searchRepository)
    }
}
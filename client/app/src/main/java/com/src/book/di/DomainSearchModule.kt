package com.src.book.di

import com.src.book.domain.repository.BookRepository
import com.src.book.domain.usecase.search.GetAllGenresUseCase
import com.src.book.domain.usecase.search.GetAllTagsUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainSearchModule {
    @Singleton
    @Provides
    fun provideGetAllGenresUseCase(bookRepository: BookRepository): GetAllGenresUseCase {
        return GetAllGenresUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllTagsUseCase(bookRepository: BookRepository): GetAllTagsUseCase {
        return GetAllTagsUseCase(bookRepository = bookRepository)
    }
}
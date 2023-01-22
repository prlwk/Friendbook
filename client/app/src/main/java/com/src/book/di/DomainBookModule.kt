package com.src.book.di

import com.src.book.domain.repository.BookRepository
import com.src.book.domain.usecase.book.GetBookByIdUseCase
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.domain.usecase.book.SetBookmarkUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainBookModule {
    @Singleton
    @Provides
    fun provideGetBooksByAuthorIdUseCase(bookRepository: BookRepository): GetBooksByAuthorIdUseCase {
        return GetBooksByAuthorIdUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideGetBookByIdUseCase(bookRepository: BookRepository): GetBookByIdUseCase {
        return GetBookByIdUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideSetBookmarkUseCase(bookRepository: BookRepository): SetBookmarkUseCase {
        return SetBookmarkUseCase(bookRepository = bookRepository)
    }
}
package com.src.book.di

import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.usecase.GetAuthorUseCase
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideGetAuthorUseCase(authorRepository: AuthorRepository): GetAuthorUseCase {
        return GetAuthorUseCase(authorRepository = authorRepository)
    }

    @Singleton
    @Provides
    fun provideGetBooksByAuthorIdUseCase(bookRepository: BookRepository): GetBooksByAuthorIdUseCase {
        return GetBooksByAuthorIdUseCase(bookRepository = bookRepository)
    }
}
package com.src.book.di

import com.src.book.domain.repository.BookRepository
import com.src.book.domain.usecase.book.*
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

    @Singleton
    @Provides
    fun provideSearchBooksUseCase(bookRepository: BookRepository): SearchBooksUseCase {
        return SearchBooksUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideSearchBooksWithPaginationUseCase(bookRepository: BookRepository): SearchBooksWithPaginationUseCase {
        return SearchBooksWithPaginationUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideGetPopularGenresUseCase(bookRepository: BookRepository): GetPopularGenresUseCase {
        return GetPopularGenresUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideGetPopularTagsUseCase(bookRepository: BookRepository): GetPopularTagsUseCase {
        return GetPopularTagsUseCase(bookRepository = bookRepository)
    }

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
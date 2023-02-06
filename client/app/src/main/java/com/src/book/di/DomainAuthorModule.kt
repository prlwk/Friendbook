package com.src.book.di

import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.usecase.author.GetAuthorUseCase
import com.src.book.domain.usecase.author.SearchAuthorsUseCase
import com.src.book.domain.usecase.author.SearchAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.author.SearchTopAuthorsWithPaginationUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainAuthorModule {
    @Singleton
    @Provides
    fun provideGetAuthorUseCase(authorRepository: AuthorRepository): GetAuthorUseCase {
        return GetAuthorUseCase(authorRepository = authorRepository)
    }

    @Singleton
    @Provides
    fun provideSearchAuthorsUseCase(authorRepository: AuthorRepository): SearchAuthorsUseCase {
        return SearchAuthorsUseCase(authorRepository = authorRepository)
    }

    @Singleton
    @Provides
    fun provideSearchAuthorsWithPaginationUseCase(authorRepository: AuthorRepository): SearchAuthorsWithPaginationUseCase {
        return SearchAuthorsWithPaginationUseCase(authorRepository = authorRepository)
    }

    @Singleton
    @Provides
    fun provideSearchTopAuthorsWithPaginationUseCase(authorRepository: AuthorRepository): SearchTopAuthorsWithPaginationUseCase {
        return SearchTopAuthorsWithPaginationUseCase(authorRepository = authorRepository)
    }
}
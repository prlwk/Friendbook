package com.src.book.di

import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.usecase.author.GetAuthorUseCase
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
}
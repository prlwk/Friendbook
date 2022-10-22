package com.src.book.di

import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.usecase.GetAuthorUseCase
import com.src.book.domain.usecase.GetBookByIdUseCase
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import com.src.book.domain.usecase.SignInUseCase
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

    @Singleton
    @Provides
    fun provideGetBookByIdUseCase(bookRepository: BookRepository): GetBookByIdUseCase {
        return GetBookByIdUseCase(bookRepository = bookRepository)
    }

    @Singleton
    @Provides
    fun provideSignInUseCase(loginRepository: LoginRepository): SignInUseCase {
        return SignInUseCase(loginRepository = loginRepository)
    }
}
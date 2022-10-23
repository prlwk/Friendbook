package com.src.book.di

import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.usecase.*
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

    @Singleton
    @Provides
    fun provideChangePasswordUseCase(userRepository: UserRepository): ChangePasswordUseCase {
        return ChangePasswordUseCase(userRepository = userRepository)
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
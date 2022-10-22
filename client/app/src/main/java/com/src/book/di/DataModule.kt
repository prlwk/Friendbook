package com.src.book.di

import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.repository.AuthorRepositoryImpl
import com.src.book.data.repository.BookRepositoryImpl
import com.src.book.data.repository.LoginRepositoryImpl
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    fun provideAuthorRepository(authorDataSource: AuthorDataSource): AuthorRepository {
        return AuthorRepositoryImpl(authorDataSource)
    }

    @Provides
    fun provideBookRepository(bookDataSource: BookDataSource): BookRepository {
        return BookRepositoryImpl(bookDataSource)
    }

    @Provides
    fun provideLoginRepository(loginDataSource: LoginDataSource): LoginRepository {
        return LoginRepositoryImpl(loginDataSource)
    }
}
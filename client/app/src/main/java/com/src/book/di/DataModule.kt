package com.src.book.di

import com.src.book.data.local.LocalUserRepository
import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.repository.*
import com.src.book.domain.repository.*
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
    fun provideLoginRepository(
        loginDataSource: LoginDataSource,
        localUserRepository: LocalUserRepository
    ): LoginRepository {
        return LoginRepositoryImpl(loginDataSource, localUserRepository)
    }

    @Provides
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    fun provideFriendRepository(friendDataSource: FriendDataSource): FriendRepository {
        return FriendRepositoryImpl(friendDataSource)
    }
}
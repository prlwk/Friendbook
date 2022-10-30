package com.src.book.di

import com.src.book.domain.repository.*
import com.src.book.domain.usecase.author.GetAuthorUseCase
import com.src.book.domain.usecase.book.GetBookByIdUseCase
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.domain.usecase.friend.*
import com.src.book.domain.usecase.login.CheckEmailExistsUseCase
import com.src.book.domain.usecase.login.CheckRecoveryCodeUseCase
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import com.src.book.domain.usecase.login.SignInUseCase
import com.src.book.domain.usecase.search.GetAllGenresUseCase
import com.src.book.domain.usecase.search.GetAllTagsUseCase
import com.src.book.domain.usecase.user.ChangePasswordUseCase
import com.src.book.domain.usecase.user.LogoutUseCase
import com.src.book.domain.usecase.friend.SendFriendRequestUseCase
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

    @Singleton
    @Provides
    fun provideCheckEmailExistsUseCase(loginRepository: LoginRepository): CheckEmailExistsUseCase {
        return CheckEmailExistsUseCase(loginRepository = loginRepository)
    }

    @Singleton
    @Provides
    fun provideLogoutUseCase(userRepository: UserRepository): LogoutUseCase {
        return LogoutUseCase(userRepository = userRepository)
    }

    @Singleton
    @Provides
    fun provideSendFriendRequestUseCase(friendRepository: FriendRepository): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideCheckRecoveryCodeUseCase(loginRepository: LoginRepository): CheckRecoveryCodeUseCase {
        return CheckRecoveryCodeUseCase(loginRepository = loginRepository)
    }

    @Singleton
    @Provides
    fun provideSendCodeForRecoveryPasswordUseCase(loginRepository: LoginRepository): SendCodeForRecoveryPasswordUseCase {
        return SendCodeForRecoveryPasswordUseCase(loginRepository = loginRepository)
    }

    @Singleton
    @Provides
    fun provideGetIncomingRequestsUseCase(friendRepository: FriendRepository): GetIncomingRequestsUseCase {
        return GetIncomingRequestsUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideSubmitFriendRequestUseCase(friendRepository: FriendRepository): SubmitFriendRequestUseCase {
        return SubmitFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideRejectIncomingFriendRequestUseCase(friendRepository: FriendRepository): RejectIncomingFriendRequestUseCase {
        return RejectIncomingFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideGetOutgoingRequestsUseCase(friendRepository: FriendRepository): GetOutgoingRequestUseCase {
        return GetOutgoingRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideRejectOutgoingFriendRequestUseCase(friendRepository: FriendRepository): RejectOutgoingFriendRequestUseCase {
        return RejectOutgoingFriendRequestUseCase(friendRepository = friendRepository)
    }
}
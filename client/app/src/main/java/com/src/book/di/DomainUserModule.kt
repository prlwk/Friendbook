package com.src.book.di

import com.src.book.domain.repository.UserRepository
import com.src.book.domain.usecase.user.ChangePasswordUseCase
import com.src.book.domain.usecase.user.EditProfileUseCase
import com.src.book.domain.usecase.user.GetProfileUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainUserModule {
    @Singleton
    @Provides
    fun provideChangePasswordUseCase(userRepository: UserRepository): ChangePasswordUseCase {
        return ChangePasswordUseCase(userRepository = userRepository)
    }

    @Singleton
    @Provides
    fun provideEditProfileUseCase(userRepository: UserRepository): EditProfileUseCase {
        return EditProfileUseCase(userRepository = userRepository)
    }

    @Singleton
    @Provides
    fun provideGetProfileUseCase(userRepository: UserRepository): GetProfileUseCase {
        return GetProfileUseCase(userRepository = userRepository)
    }
}
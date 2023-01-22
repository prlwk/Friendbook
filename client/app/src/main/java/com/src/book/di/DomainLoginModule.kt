package com.src.book.di

import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.usecase.login.*
import com.src.book.domain.usecase.user.LogoutUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainLoginModule {
    @Singleton
    @Provides
    fun provideSignInUseCase(loginRepository: LoginRepository): SignInUseCase {
        return SignInUseCase(loginRepository = loginRepository)
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
    fun provideRegistrationUseCase(loginRepository: LoginRepository): RegistrationUseCase {
        return RegistrationUseCase(loginRepository = loginRepository)
    }

    @Singleton
    @Provides
    fun provideCheckRecoveryCodeForConfirmationsUseCase(loginRepository: LoginRepository): CheckRecoveryCodeForConfirmationsUseCase {
        return CheckRecoveryCodeForConfirmationsUseCase(loginRepository = loginRepository)
    }

    @Singleton
    @Provides
    fun provideSendCodeForConfirmationsUseCase(loginRepository: LoginRepository): SendCodeForConfirmationsUseCase {
        return SendCodeForConfirmationsUseCase(loginRepository = loginRepository)
    }

    @Singleton
    @Provides
    fun provideLoginAsGuestUseCase(loginRepository: LoginRepository): LoginAsGuestUseCase {
        return LoginAsGuestUseCase(loginRepository = loginRepository)
    }
}
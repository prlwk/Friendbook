package com.src.book.presentation.registration.sign_in.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.login.LoginAsGuestUseCase
import com.src.book.domain.usecase.login.SignInUseCase
import javax.inject.Inject

class SignInViewModelFactory @Inject constructor(private val signInUseCase: SignInUseCase, private val loginAsGuestUseCase: LoginAsGuestUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(signInUseCase = signInUseCase, loginAsGuestUseCase = loginAsGuestUseCase) as T
    }
}
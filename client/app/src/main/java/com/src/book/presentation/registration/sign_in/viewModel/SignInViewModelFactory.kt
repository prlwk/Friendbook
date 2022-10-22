package com.src.book.presentation.registration.sign_in.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.SignInUseCase
import javax.inject.Inject

class SignInViewModelFactory @Inject constructor(val signInUseCase: SignInUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(signInUseCase = signInUseCase) as T
    }
}
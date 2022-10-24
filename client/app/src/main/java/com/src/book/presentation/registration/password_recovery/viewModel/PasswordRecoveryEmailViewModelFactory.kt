package com.src.book.presentation.registration.password_recovery.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.login.CheckEmailExistsUseCase
import javax.inject.Inject

class PasswordRecoveryEmailViewModelFactory @Inject constructor(private val checkEmailExistsUseCase: CheckEmailExistsUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordRecoveryEmailViewModel(checkEmailExistsUseCase = checkEmailExistsUseCase) as T
    }
}
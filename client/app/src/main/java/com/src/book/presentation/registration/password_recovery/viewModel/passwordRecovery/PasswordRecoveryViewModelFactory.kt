package com.src.book.presentation.registration.password_recovery.viewModel.passwordRecovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.user.ChangePasswordUseCase
import javax.inject.Inject

class PasswordRecoveryViewModelFactory @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordRecoveryViewModel(changePasswordUseCase = changePasswordUseCase) as T
    }
}
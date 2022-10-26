package com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryEmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import javax.inject.Inject

class PasswordRecoveryEmailViewModelFactory @Inject constructor(
    private val sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordRecoveryEmailViewModel(sendCodeForRecoveryPasswordUseCase = sendCodeForRecoveryPasswordUseCase) as T
    }
}
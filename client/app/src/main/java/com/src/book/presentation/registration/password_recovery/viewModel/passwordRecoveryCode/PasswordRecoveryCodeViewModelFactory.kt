package com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.login.CheckRecoveryCodeUseCase
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import javax.inject.Inject

class PasswordRecoveryCodeViewModelFactory @Inject constructor(
    private val checkRecoveryCodeUseCase: CheckRecoveryCodeUseCase,
    private val sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordRecoveryCodeViewModel(
            checkRecoveryCodeUseCase = checkRecoveryCodeUseCase,
            sendCodeForRecoveryPasswordUseCase = sendCodeForRecoveryPasswordUseCase
        ) as T
    }
}
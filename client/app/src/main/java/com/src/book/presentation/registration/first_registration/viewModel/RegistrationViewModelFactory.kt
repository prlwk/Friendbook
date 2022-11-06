package com.src.book.presentation.registration.first_registration.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.login.CheckEmailExistsUseCase
import com.src.book.domain.usecase.login.CheckRecoveryCodeForConfirmationsUseCase
import com.src.book.domain.usecase.login.RegistrationUseCase
import com.src.book.domain.usecase.login.SendCodeForConfirmationsUseCase
import javax.inject.Inject

class RegistrationViewModelFactory @Inject constructor(
    private val emailExistsUseCase: CheckEmailExistsUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val checkRecoveryCodeForConfirmationsUseCase: CheckRecoveryCodeForConfirmationsUseCase,
    private val sendCodeForConfirmationsUseCase: SendCodeForConfirmationsUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(
            checkEmailExistsUseCase = emailExistsUseCase,
            registrationUseCase = registrationUseCase,
            checkRecoveryCodeForConfirmationsUseCase = checkRecoveryCodeForConfirmationsUseCase,
            sendCodeForConfirmationsUseCase = sendCodeForConfirmationsUseCase
        ) as T
    }
}
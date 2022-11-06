package com.src.book.presentation.registration.first_registration.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.login.*
import javax.inject.Inject

class RegistrationViewModelFactory @Inject constructor(
    private val emailExistsUseCase: CheckEmailExistsUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val checkRecoveryCodeForConfirmationsUseCase: CheckRecoveryCodeForConfirmationsUseCase,
    private val sendCodeForConfirmationsUseCase: SendCodeForConfirmationsUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(
            checkEmailExistsUseCase = emailExistsUseCase,
            registrationUseCase = registrationUseCase,
            checkRecoveryCodeForConfirmationsUseCase = checkRecoveryCodeForConfirmationsUseCase,
            sendCodeForConfirmationsUseCase = sendCodeForConfirmationsUseCase,
            loginAsGuestUseCase = loginAsGuestUseCase
        ) as T
    }
}
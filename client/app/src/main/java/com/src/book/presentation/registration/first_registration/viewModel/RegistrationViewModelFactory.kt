package com.src.book.presentation.registration.first_registration.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.CheckEmailExistsUseCase
import javax.inject.Inject

class RegistrationViewModelFactory @Inject constructor(private val emailExistsUseCase: CheckEmailExistsUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(checkEmailExistsUseCase = emailExistsUseCase) as T
    }
}
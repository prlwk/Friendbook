package com.src.book.presentation.profile.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.LogoutUseCase
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(private val logoutUseCase: LogoutUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(logoutUseCase) as T
    }
}
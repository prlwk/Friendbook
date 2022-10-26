package com.src.book.presentation.registration.password_recovery.viewModel.passwordRecovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.user.ChangePasswordUseCase
import com.src.book.domain.utils.ChangePasswordState
import kotlinx.coroutines.launch

class PasswordRecoveryViewModel(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableLiveDataChangePasswordState =
        MutableLiveData<ChangePasswordState>(ChangePasswordState.SuccessState)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataChangePasswordState get() = _mutableLiveDataChangePasswordState

    fun changePassword(password: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataChangePasswordState.value =
                changePasswordUseCase.execute(oldPassword = null, newPassword = password)
            _mutableLiveDataIsLoading.value = false
        }
    }
}
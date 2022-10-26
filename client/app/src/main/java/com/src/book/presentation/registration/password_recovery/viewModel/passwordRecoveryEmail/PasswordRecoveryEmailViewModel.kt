package com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryEmail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.login.CheckEmailExistsUseCase
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import com.src.book.domain.utils.CodeState
import kotlinx.coroutines.launch

class PasswordRecoveryEmailViewModel(private val sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase
) :
    ViewModel() {
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableLiveDataCodeState = MutableLiveData<CodeState>(CodeState.SuccessState)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataCodeState get() = _mutableLiveDataCodeState

    fun sendCode(email: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataCodeState.value = sendCodeForRecoveryPasswordUseCase.execute(email)
            _mutableLiveDataIsLoading.value = false
        }
    }
}
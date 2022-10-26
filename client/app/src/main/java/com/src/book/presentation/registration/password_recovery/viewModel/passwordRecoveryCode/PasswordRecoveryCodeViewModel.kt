package com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryCode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.login.CheckRecoveryCodeUseCase
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import com.src.book.domain.utils.CodeState
import kotlinx.coroutines.launch

class PasswordRecoveryCodeViewModel(
    private val checkRecoveryCodeUseCase: CheckRecoveryCodeUseCase,
    private val sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase
) :
    ViewModel() {
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableLiveDataCodeState = MutableLiveData<CodeState>(CodeState.SuccessState)
    private val _mutableLiveDataRepeatingCodeState =
        MutableLiveData<CodeState>(CodeState.SuccessState)
    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataCodeState get() = _mutableLiveDataCodeState
    val liveDataRepeatingCodeState get() = _mutableLiveDataRepeatingCodeState

    fun checkRecoveryCode(code: String, email: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataCodeState.value =
                checkRecoveryCodeUseCase.execute(code = code, email = email)
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun sendRepeatingCodeState(email: String) {
        viewModelScope.launch {
            _mutableLiveDataRepeatingCodeState.value = CodeState.LoadingState
            _mutableLiveDataRepeatingCodeState.value =
                sendCodeForRecoveryPasswordUseCase.execute(email)
        }
    }
}
package com.src.book.presentation.registration.sign_in.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.SignInUseCase
import com.src.book.domain.utils.LoginState
import kotlinx.coroutines.launch

class SignInViewModel(val signInUseCase: SignInUseCase) : ViewModel() {
    private val _mutableLoginState = MutableLiveData<LoginState>(LoginState.SuccessState)
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataLoginState get() = _mutableLoginState

    fun signInRequest(email: String, password: String, isEntryByEmail: Boolean) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLoginState.value = signInUseCase.execute(email, password, isEntryByEmail)
            _mutableLiveDataIsLoading.value = false
        }
    }
}
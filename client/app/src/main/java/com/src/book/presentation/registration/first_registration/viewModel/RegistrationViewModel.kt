package com.src.book.presentation.registration.first_registration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.CheckEmailExistsUseCase
import kotlinx.coroutines.launch

class RegistrationViewModel(val checkEmailExistsUseCase: CheckEmailExistsUseCase) : ViewModel() {
    private val _mutableEmailExists = MutableLiveData(false)
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataEmailExists get() = _mutableEmailExists

    fun checkEmailExists(email: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableEmailExists.value = checkEmailExistsUseCase.execute(email)
            _mutableLiveDataIsLoading.value = false
        }
    }
}
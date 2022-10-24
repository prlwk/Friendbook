package com.src.book.presentation.profile.settings.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.LogoutUseCase
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.launch

class SettingsViewModel(private val logoutUseCase: LogoutUseCase) : ViewModel() {
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableBasicState = MutableLiveData<BasicState>(BasicState.SuccessState)

    val liveDataBasicState get() = _mutableBasicState
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun logout() {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableBasicState.value = logoutUseCase.execute()
            _mutableLiveDataIsLoading.value = false
        }
    }
}
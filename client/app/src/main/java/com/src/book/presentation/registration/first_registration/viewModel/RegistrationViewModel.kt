package com.src.book.presentation.registration.first_registration.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.login.CheckEmailExistsUseCase
import com.src.book.domain.usecase.login.CheckRecoveryCodeForConfirmationsUseCase
import com.src.book.domain.usecase.login.RegistrationUseCase
import com.src.book.domain.usecase.login.SendCodeForConfirmationsUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.RegistrationState
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val checkRecoveryCodeForConfirmationsUseCase: CheckRecoveryCodeForConfirmationsUseCase,
    private val sendCodeForConfirmationsUseCase: SendCodeForConfirmationsUseCase
) : ViewModel() {
    private val _mutableEmailExists = MutableLiveData<BasicState>(BasicState.DefaultState)
    private val _mutableLiveDataIsLoading = MutableLiveData(false)
    private val _mutableLiveDataRegistration =
        MutableLiveData<RegistrationState>(RegistrationState.DefaultState)

    private val _mutableLiveDataImage = MutableLiveData<Uri?>(null)
    private val _mutableLiveDataEmail = MutableLiveData<String?>(null)
    private val _mutableLiveDataPassword = MutableLiveData<String?>(null)
    private val _mutableLiveDataName = MutableLiveData<String?>(null)
    private val _mutableLiveDataLogin = MutableLiveData<String?>(null)

    private val _mutableLiveDataCodeState = MutableLiveData<CodeState>(CodeState.DefaultState)
    private val _mutableLiveDataRepeatingCodeState =
        MutableLiveData<BasicState>(BasicState.DefaultState)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataEmailExists get() = _mutableEmailExists
    val liveDataRegistration get() = _mutableLiveDataRegistration

    val liveDataImage get() = _mutableLiveDataImage
    val liveDataEmail get() = _mutableLiveDataEmail
    val liveDataPassword get() = _mutableLiveDataPassword
    val liveDataLogin get() = _mutableLiveDataLogin
    val liveDataName get() = _mutableLiveDataName

    val liveDataCodeState get() = _mutableLiveDataCodeState
    val liveDataRepeatingCodeState get() = _mutableLiveDataRepeatingCodeState

    fun checkEmailExists(email: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableEmailExists.value = checkEmailExistsUseCase.execute(email)
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun registration(data: String, uri: Uri?) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataRegistration.value = RegistrationState.DefaultState
            _mutableLiveDataRegistration.value = registrationUseCase.execute(data, uri)
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun setImage(uri: Uri) {
        _mutableLiveDataImage.value = uri
    }

    fun imageIsNotEmpty(): Boolean {
        return _mutableLiveDataImage.value != null
    }

    fun setEmail(email: String) {
        _mutableLiveDataEmail.value = email
    }

    fun setLogin(login: String) {
        _mutableLiveDataLogin.value = login
    }

    fun setPassword(password: String) {
        _mutableLiveDataPassword.value = password
    }

    fun setName(name: String) {
        _mutableLiveDataName.value = name
    }

    fun checkRecoveryCode(code: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataCodeState.value =
                checkRecoveryCodeForConfirmationsUseCase.execute(
                    code = code,
                    email = _mutableLiveDataEmail.value!!
                )
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun sendRepeatingCodeState() {
        viewModelScope.launch {
            _mutableLiveDataRepeatingCodeState.value = BasicState.DefaultState
            _mutableLiveDataRepeatingCodeState.value =
                sendCodeForConfirmationsUseCase.execute()
        }
    }

    fun setDefaultValueForCodeState() {
        _mutableLiveDataRepeatingCodeState.value = BasicState.DefaultState
    }
}
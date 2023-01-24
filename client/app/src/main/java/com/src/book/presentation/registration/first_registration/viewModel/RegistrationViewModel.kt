package com.src.book.presentation.registration.first_registration.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.login.*
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.RegistrationState
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val checkRecoveryCodeForConfirmationsUseCase: CheckRecoveryCodeForConfirmationsUseCase,
    private val sendCodeForConfirmationsUseCase: SendCodeForConfirmationsUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase
) : ViewModel() {
    private val _mutableEmailExists =
        MutableLiveData<BasicState<Boolean>>(BasicState.DefaultState())
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
        MutableLiveData<BasicState<Unit>>(BasicState.DefaultState())

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

    fun registration(uri: Uri?) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataRegistration.value = RegistrationState.DefaultState
            if (_mutableLiveDataEmail.value != null && _mutableLiveDataPassword.value != null && _mutableLiveDataLogin.value != null && _mutableLiveDataName.value != null) {
                _mutableLiveDataRegistration.value = registrationUseCase.execute(
                    email = _mutableLiveDataEmail.value!!,
                    password = _mutableLiveDataPassword.value!!,
                    login = _mutableLiveDataLogin.value!!,
                    name = _mutableLiveDataName.value!!,
                    uri = uri
                )
            } else {
                _mutableLiveDataRegistration.value = RegistrationState.ErrorState
            }
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

    fun sendRepeatingCode() {
        viewModelScope.launch {
            _mutableLiveDataRepeatingCodeState.value = BasicState.DefaultState()
            _mutableLiveDataRepeatingCodeState.value =
                sendCodeForConfirmationsUseCase.execute()
        }
    }

    fun setDefaultValueForCodeState() {
        _mutableLiveDataRepeatingCodeState.value = BasicState.DefaultState()
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            loginAsGuestUseCase.execute()
        }
    }
}
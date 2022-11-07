package com.src.book.presentation.profile.my_profile.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.user.EditProfileUseCase
import com.src.book.domain.utils.EditProfileState
import kotlinx.coroutines.launch

class MyProfileViewModel(private val editProfileUseCase: EditProfileUseCase) : ViewModel() {
    private val _mutableLiveDataIsLoading = MutableLiveData(false)
    private val _mutableLiveDataEditProfileState =
        MutableLiveData<EditProfileState>(EditProfileState.DefaultState)

    private val _mutableLiveDataName = MutableLiveData<String?>(null)
    private val _mutableLiveDataLogin = MutableLiveData<String?>(null)
    private val _mutableLiveDataPhoto = MutableLiveData<Uri?>(null)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataEditProfileState get() = _mutableLiveDataEditProfileState

    val liveDataName get() = _mutableLiveDataName
    val liveDataLogin get() = _mutableLiveDataLogin
    val liveDataPhoto get() = _mutableLiveDataPhoto

    fun editProfile() {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataEditProfileState.value = EditProfileState.DefaultState
            if (_mutableLiveDataLogin.value != null && _mutableLiveDataName.value != null) {
                _mutableLiveDataEditProfileState.value =
                    editProfileUseCase.execute(
                        login = _mutableLiveDataLogin.value!!,
                        name = _mutableLiveDataName.value!!,
                        uri = _mutableLiveDataPhoto.value
                    )
            } else {
                _mutableLiveDataEditProfileState.value = EditProfileState.ErrorState
            }
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun setName(name: String) {
        _mutableLiveDataName.value = name
    }

    fun setLogin(login: String) {
        _mutableLiveDataLogin.value = login
    }

    fun setPhoto(uri: Uri?) {
        _mutableLiveDataPhoto.value = uri
    }

}
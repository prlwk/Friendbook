package com.src.book.presentation.profile.my_profile.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.user.UserProfile
import com.src.book.domain.usecase.user.EditProfileUseCase
import com.src.book.domain.usecase.user.GetProfileUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.EditProfileState
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val editProfileUseCase: EditProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    private val _mutableLiveDataIsLoading = MutableLiveData(false)
    private val _mutableLiveDataEditProfileState =
        MutableLiveData<EditProfileState>(EditProfileState.DefaultState)
    private val _mutableLiveDataProfileState = MutableLiveData<BasicState>(BasicState.DefaultState)

    private val _mutableLiveDataName = MutableLiveData<String?>(null)
    private val _mutableLiveDataLogin = MutableLiveData<String?>(null)
    private val _mutableLiveDataPhotoUri = MutableLiveData<Uri?>(null)
    private val _mutableLiveDataPhotoString = MutableLiveData<String?>(null)

    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataEditProfileState get() = _mutableLiveDataEditProfileState
    val liveDataProfileState get() = _mutableLiveDataProfileState

    fun editProfile() {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataEditProfileState.value = EditProfileState.DefaultState
            if (_mutableLiveDataLogin.value != null && _mutableLiveDataName.value != null) {
                _mutableLiveDataEditProfileState.value =
                    editProfileUseCase.execute(
                        login = _mutableLiveDataLogin.value!!,
                        name = _mutableLiveDataName.value!!,
                        uri = _mutableLiveDataPhotoUri.value
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
        _mutableLiveDataPhotoUri.value = uri
    }

    fun setProfile() {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataProfileState.value = BasicState.DefaultState
            _mutableLiveDataProfileState.value = getProfileUseCase.execute()
            if (_mutableLiveDataProfileState.value is BasicState.SuccessStateWithResources<*>) {
                val profile =
                    (_mutableLiveDataProfileState.value as BasicState.SuccessStateWithResources<*>).data as UserProfile
                _mutableLiveDataName.value = profile.name
                _mutableLiveDataLogin.value = profile.login
                _mutableLiveDataPhotoString.value = profile.image
            }
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun getName(): String? {
        return _mutableLiveDataName.value
    }

    fun getLogin(): String? {
        return _mutableLiveDataLogin.value
    }

    fun getImageString(): String? {
        return _mutableLiveDataPhotoString.value
    }

    fun setEditProfileState() {
        _mutableLiveDataEditProfileState.value = EditProfileState.DefaultState
    }

}
package com.src.book.presentation.profile.my_profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.user.EditProfileUseCase
import com.src.book.domain.usecase.user.GetProfileUseCase
import javax.inject.Inject

class MyProfileViewModelFactory @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyProfileViewModel(
            editProfileUseCase = editProfileUseCase,
            getProfileUseCase = getProfileUseCase
        ) as T
    }
}
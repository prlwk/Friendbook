package com.src.book.domain.utils

sealed class EditProfileState {
    object SuccessState : EditProfileState()
    object ErrorState : EditProfileState()
    object LoginAlreadyExistsState : EditProfileState()
    object DefaultState:EditProfileState()
}
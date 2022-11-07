package com.src.book.domain.utils

sealed class RegistrationState {
    object SuccessState : RegistrationState()
    object EmailAlreadyExistsState : RegistrationState()
    object LoginAlreadyExistsState : RegistrationState()
    object ErrorState : RegistrationState()
    object DefaultState : RegistrationState()
}
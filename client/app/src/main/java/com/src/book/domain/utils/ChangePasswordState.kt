package com.src.book.domain.utils

sealed class ChangePasswordState {
    object SuccessState : ChangePasswordState()
    object ErrorState : ChangePasswordState()
    object WrongPasswordState : ChangePasswordState()
}
package com.src.book.domain.utils

sealed class LoginState {
    object ErrorServerState : LoginState()
    object ErrorPasswordState : LoginState()
    object SuccessState : LoginState()
    object ErrorEmailLoginState : LoginState()
}
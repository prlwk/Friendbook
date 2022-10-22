package com.src.book.domain.utils

sealed class BasicState {
    object SuccessState : BasicState()
    object ErrorState : BasicState()
}
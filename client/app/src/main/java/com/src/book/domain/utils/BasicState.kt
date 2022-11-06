package com.src.book.domain.utils

sealed class BasicState {
    object SuccessState : BasicState()
    object ErrorState : BasicState()
    class SuccessStateWithResources<T>(val data: T) : BasicState()
    object DefaultState : BasicState()
}
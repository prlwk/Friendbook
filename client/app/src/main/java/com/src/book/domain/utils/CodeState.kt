package com.src.book.domain.utils

sealed class CodeState {
    object SuccessState : CodeState()
    object ErrorState : CodeState()
    object WrongCodeState : CodeState()
    object WrongEmailState : CodeState()
    object LoadingState : CodeState()
    object DefaultState : CodeState()
}
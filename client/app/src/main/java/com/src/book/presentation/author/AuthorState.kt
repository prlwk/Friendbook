package com.src.book.presentation.author

import com.src.book.domain.model.Author

sealed class AuthorState {
    class SuccessState(val author: Author?) : AuthorState()
    class ErrorState(val author: Author?) : AuthorState()
    object DefaultState : AuthorState()
}
package com.src.book.domain.utils


sealed class BookmarkState {
    object SuccessState : BookmarkState()
    object ErrorState : BookmarkState()
    object NotAuthorizedState : BookmarkState()
    object DefaultState : BookmarkState()
}
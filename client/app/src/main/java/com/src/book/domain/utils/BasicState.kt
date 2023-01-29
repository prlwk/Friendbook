package com.src.book.domain.utils

sealed class BasicState<out T : Any> {
    class SuccessState<out T : Any>(val data: T) : BasicState<T>()
    class EmptyState<out T : Any>() : BasicState<T>()
    class ErrorState<out T : Any> : BasicState<T>()
    class DefaultState<out T : Any> : BasicState<T>()
    class LoadingState<out T : Any> : BasicState<T>()
}
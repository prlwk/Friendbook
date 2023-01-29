package com.src.book.presentation.book.main_page

import com.src.book.domain.model.book.Book

sealed class BookState {
    class DefaultState(val book: Book?) : BookState()
    class ErrorState(val book: Book?) : BookState()
}
package com.src.book.presentation.book

import com.src.book.domain.model.Book

sealed class BookState {
    class DefaultState(val books: List<Book>?) : BookState()
    class ErrorState(val books: List<Book>?) : BookState()
}
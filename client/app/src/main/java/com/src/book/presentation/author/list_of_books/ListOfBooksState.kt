package com.src.book.presentation.author.list_of_books

import com.src.book.domain.model.Book

sealed class ListOfBooksState {
    class DefaultState(val books: List<Book>?) : ListOfBooksState()
    class ErrorState(val books: List<Book>?) : ListOfBooksState()
}
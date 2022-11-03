package com.src.book.presentation.author.list_of_books

import com.src.book.domain.model.BookList

sealed class ListOfBooksState {
    class SuccessState(val books: List<BookList>) : ListOfBooksState()
    object ErrorState : ListOfBooksState()
    object DefaultState : ListOfBooksState()
}
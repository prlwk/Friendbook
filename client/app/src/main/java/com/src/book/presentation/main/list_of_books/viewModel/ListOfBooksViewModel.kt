package com.src.book.presentation.main.list_of_books.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import com.src.book.presentation.book.BookState
import kotlinx.coroutines.launch

class ListOfBooksViewModel(private val getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase) :
    ViewModel() {
    private val _mutableLiveDataBooks = MutableLiveData<BookState>(BookState.DefaultState(null))
    val liveDataBooks get() = _mutableLiveDataBooks
    fun loadBooksByAuthorId(id: Long) {
        viewModelScope.launch {
            val books = getBooksByAuthorIdUseCase.execute(id)
            if (books == null) {
                _mutableLiveDataBooks.value = BookState.ErrorState(null)
            } else {
                _mutableLiveDataBooks.value = BookState.DefaultState(books)
            }
        }
    }

}
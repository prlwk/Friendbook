package com.src.book.presentation.book.main_page.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.GetBookByIdUseCase
import com.src.book.presentation.book.main_page.BookState
import kotlinx.coroutines.launch

class BookViewModel(private val getBookByIdUseCase: GetBookByIdUseCase) : ViewModel() {
    private val _mutableLiveDataBook =
        MutableLiveData<BookState>(BookState.DefaultState(null))
    val liveDataBook get() = _mutableLiveDataBook

    fun loadBookById(id: Long) {
        viewModelScope.launch {
            val book = getBookByIdUseCase.execute(id)
            if (book == null) {
                _mutableLiveDataBook.value = BookState.ErrorState(null)
            } else {
                _mutableLiveDataBook.value = BookState.DefaultState(book)
            }
        }
    }
}
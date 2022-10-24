package com.src.book.presentation.book.main_page.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.book.GetBookByIdUseCase
import com.src.book.presentation.book.main_page.BookState
import kotlinx.coroutines.launch

class BookViewModel(private val getBookByIdUseCase: GetBookByIdUseCase) : ViewModel() {
    private val _mutableLiveDataBook =
        MutableLiveData<BookState>(BookState.DefaultState(null))
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    val liveDataBook get() = _mutableLiveDataBook
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun loadBookById(id: Long) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            val book = getBookByIdUseCase.execute(id)
            if (book == null) {
                _mutableLiveDataBook.value = BookState.ErrorState(null)
            } else {
                _mutableLiveDataBook.value = BookState.DefaultState(book)
            }
            _mutableLiveDataIsLoading.value = false
        }
    }
}
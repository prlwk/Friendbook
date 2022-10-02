package com.src.book.presentation.main.list_of_books.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import com.src.book.presentation.main.list_of_books.ListOfBooksState
import kotlinx.coroutines.launch

class ListOfBooksViewModel(private val getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase) :
    ViewModel() {
    private val _mutableLiveDataBooks =
        MutableLiveData<ListOfBooksState>(ListOfBooksState.DefaultState(null))
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    val liveDataBooks get() = _mutableLiveDataBooks
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun loadBooksByAuthorId(id: Long) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            val books = getBooksByAuthorIdUseCase.execute(id)
            if (books == null) {
                _mutableLiveDataBooks.value = ListOfBooksState.ErrorState(null)
            } else {
                _mutableLiveDataBooks.value = ListOfBooksState.DefaultState(books)
            }
            _mutableLiveDataIsLoading.value = false
        }
    }

}
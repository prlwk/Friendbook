package com.src.book.presentation.author.list_of_books.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.BookList
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.author.list_of_books.ListOfBooksState
import kotlinx.coroutines.launch

class ListOfBooksViewModel(private val getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase) :
    ViewModel() {
    private val _mutableLiveDataBooks =
        MutableLiveData<ListOfBooksState>(ListOfBooksState.DefaultState)
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    val liveDataBooks get() = _mutableLiveDataBooks
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun loadBooksByAuthorId(id: Long) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataBooks.value = ListOfBooksState.DefaultState
            val state = getBooksByAuthorIdUseCase.execute(id)
            if (state is BasicState.ErrorState) {
                _mutableLiveDataBooks.value = ListOfBooksState.ErrorState
            } else {
                _mutableLiveDataBooks.value =
                    ListOfBooksState.SuccessState((state as BasicState.SuccessStateWithResources<List<BookList>>).data)
            }
            _mutableLiveDataIsLoading.value = false
        }
    }

}
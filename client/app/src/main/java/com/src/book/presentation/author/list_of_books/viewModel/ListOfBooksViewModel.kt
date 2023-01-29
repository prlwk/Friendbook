package com.src.book.presentation.author.list_of_books.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.book.BookList
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.domain.usecase.book.SetBookmarkUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState
import kotlinx.coroutines.launch

class ListOfBooksViewModel(
    private val getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase,
    private val setBookmarkUseCase: SetBookmarkUseCase
) :
    ViewModel() {
    private val _mutableLiveDataBooks =
        MutableLiveData<BasicState<List<BookList>>>(BasicState.DefaultState())
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableLiveDataSetBookmarkState =
        MutableLiveData<BookmarkState>(BookmarkState.DefaultState)
    val liveDataBooks get() = _mutableLiveDataBooks
    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    val liveDataSetBookmarkState get() = _mutableLiveDataSetBookmarkState

    fun loadBooksByAuthorId(id: Long) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataBooks.value = BasicState.DefaultState()
            _mutableLiveDataBooks.value = getBooksByAuthorIdUseCase.execute(id)
            _mutableLiveDataIsLoading.value = false
        }
    }

    fun setBookmark(bookId: Long, isSaving: Boolean) {
        viewModelScope.launch {
            _mutableLiveDataSetBookmarkState.value = BookmarkState.DefaultState
            _mutableLiveDataSetBookmarkState.value = setBookmarkUseCase.execute(bookId, isSaving)
        }
    }

}
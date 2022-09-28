package com.src.book.presentation.author.main_page.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.GetAuthorUseCase
import com.src.book.presentation.author.AuthorState
import kotlinx.coroutines.launch

class AuthorViewModel(private val getAuthorUseCase: GetAuthorUseCase) :
    ViewModel() {
    private val _mutableLiveDataAuthor =
        MutableLiveData<AuthorState>(AuthorState.DefaultState(null))
    val liveDataAuthor get() = _mutableLiveDataAuthor

    fun loadAuthorById(id: Long) {
        viewModelScope.launch {
            val author = getAuthorUseCase.getAuthor(id)
            if (author == null) {
                _mutableLiveDataAuthor.value = AuthorState.ErrorState(null)
            } else {
                _mutableLiveDataAuthor.value = AuthorState.DefaultState(author)
            }
        }
    }
}
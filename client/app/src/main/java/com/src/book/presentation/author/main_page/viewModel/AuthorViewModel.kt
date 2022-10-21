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
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    val liveDataAuthor get() = _mutableLiveDataAuthor
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun loadAuthorById(id: Long) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            val author = getAuthorUseCase.execute(id)
            if (author == null) {
                _mutableLiveDataAuthor.value = AuthorState.ErrorState(null)
            } else {
                _mutableLiveDataAuthor.value = AuthorState.DefaultState(author)
            }
            _mutableLiveDataIsLoading.value = false
        }
    }
}
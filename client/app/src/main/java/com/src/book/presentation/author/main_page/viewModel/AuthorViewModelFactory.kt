package com.src.book.presentation.author.main_page.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.author.GetAuthorUseCase
import javax.inject.Inject

class AuthorViewModelFactory @Inject constructor(val getAuthorUseCase: GetAuthorUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AuthorViewModel(getAuthorUseCase) as T
}
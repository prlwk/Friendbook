package com.src.book.presentation.author.list_of_books.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import javax.inject.Inject

class ListOfBooksViewModelFactory @Inject constructor(val getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ListOfBooksViewModel(getBooksByAuthorIdUseCase = getBooksByAuthorIdUseCase) as T
}
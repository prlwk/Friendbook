package com.src.book.presentation.book.main_page.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.GetBookByIdUseCase
import javax.inject.Inject

class BookViewModelFactory @Inject constructor(private val getBookByIdUseCase: GetBookByIdUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(getBookByIdUseCase) as T
    }
}
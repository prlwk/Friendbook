package com.src.book.presentation.main.main_page.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.author.SearchAuthorsUseCase
import com.src.book.domain.usecase.author.SearchAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.book.SearchBooksUseCase
import com.src.book.domain.usecase.book.SearchBooksWithPaginationUseCase
import javax.inject.Inject

class MainPageViewModelFactory @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchAuthorsWithPaginationUseCase: SearchAuthorsWithPaginationUseCase,
    private val searchBooksWithPaginationUseCase: SearchBooksWithPaginationUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainPageViewModel(
            searchBooksUseCase = searchBooksUseCase,
            searchAuthorsUseCase = searchAuthorsUseCase,
            searchAuthorsWithPaginationUseCase = searchAuthorsWithPaginationUseCase,
            searchBooksWithPaginationUseCase = searchBooksWithPaginationUseCase
        ) as T
    }
}
package com.src.book.presentation.main.main_page.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.author.SearchAuthorsUseCase
import com.src.book.domain.usecase.author.SearchAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.book.*
import javax.inject.Inject

class MainPageViewModelFactory @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchAuthorsWithPaginationUseCase: SearchAuthorsWithPaginationUseCase,
    private val searchBooksWithPaginationUseCase: SearchBooksWithPaginationUseCase,
    private val getPopularGenresUseCase: GetPopularGenresUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainPageViewModel(
            searchBooksUseCase = searchBooksUseCase,
            searchAuthorsUseCase = searchAuthorsUseCase,
            searchAuthorsWithPaginationUseCase = searchAuthorsWithPaginationUseCase,
            searchBooksWithPaginationUseCase = searchBooksWithPaginationUseCase,
            getPopularGenresUseCase = getPopularGenresUseCase,
            getPopularTagsUseCase = getPopularTagsUseCase,
            getAllTagsUseCase = getAllTagsUseCase,
            getAllGenresUseCase = getAllGenresUseCase
        ) as T
    }
}
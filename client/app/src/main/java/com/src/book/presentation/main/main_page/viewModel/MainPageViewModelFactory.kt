package com.src.book.presentation.main.main_page.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.author.SearchAuthorsUseCase
import com.src.book.domain.usecase.author.SearchAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.author.SearchTopAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.book.*
import com.src.book.domain.usecase.search.AddSearchItemUseCase
import com.src.book.domain.usecase.search.DeleteSearchItemByIdUseCase
import com.src.book.domain.usecase.search.GetAllSearchItemsUseCase
import javax.inject.Inject

class MainPageViewModelFactory @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchAuthorsWithPaginationUseCase: SearchAuthorsWithPaginationUseCase,
    private val searchTopAuthorsWithPaginationUseCase: SearchTopAuthorsWithPaginationUseCase,
    private val searchBooksWithPaginationUseCase: SearchBooksWithPaginationUseCase,
    private val searchTopBooksWithPaginationUseCase: SearchTopBooksWithPaginationUseCase,
    private val getPopularGenresUseCase: GetPopularGenresUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val getAllSearchItemsUseCase: GetAllSearchItemsUseCase,
    private val addSearchItemUseCase: AddSearchItemUseCase,
    private val deleteSearchItemByIdUseCase: DeleteSearchItemByIdUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainPageViewModel(
            searchBooksUseCase = searchBooksUseCase,
            searchAuthorsUseCase = searchAuthorsUseCase,
            searchAuthorsWithPaginationUseCase = searchAuthorsWithPaginationUseCase,
            searchTopAuthorsWithPaginationUseCase = searchTopAuthorsWithPaginationUseCase,
            searchBooksWithPaginationUseCase = searchBooksWithPaginationUseCase,
            searchTopBooksWithPaginationUseCase = searchTopBooksWithPaginationUseCase,
            getPopularGenresUseCase = getPopularGenresUseCase,
            getPopularTagsUseCase = getPopularTagsUseCase,
            getAllTagsUseCase = getAllTagsUseCase,
            getAllGenresUseCase = getAllGenresUseCase,
            getAllSearchItemsUseCase = getAllSearchItemsUseCase,
            addSearchItemUseCase = addSearchItemUseCase,
            deleteSearchItemByIdUseCase = deleteSearchItemByIdUseCase
        ) as T
    }
}
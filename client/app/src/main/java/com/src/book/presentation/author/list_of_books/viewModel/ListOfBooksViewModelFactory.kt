package com.src.book.presentation.author.list_of_books.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.domain.usecase.book.SetBookmarkUseCase
import javax.inject.Inject

class ListOfBooksViewModelFactory @Inject constructor(
    private val getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase,
    private val setBookmarkUseCase: SetBookmarkUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ListOfBooksViewModel(
            getBooksByAuthorIdUseCase = getBooksByAuthorIdUseCase,
            setBookmarkUseCase = setBookmarkUseCase
        ) as T
}
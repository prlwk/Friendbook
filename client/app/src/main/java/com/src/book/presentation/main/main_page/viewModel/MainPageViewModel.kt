package com.src.book.presentation.main.main_page.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.src.book.domain.author.AuthorList
import com.src.book.domain.model.book.BookList
import com.src.book.domain.usecase.author.SearchAuthorsUseCase
import com.src.book.domain.usecase.author.SearchAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.book.SearchBooksUseCase
import com.src.book.domain.usecase.book.SearchBooksWithPaginationUseCase
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchAuthorsWithPaginationUseCase: SearchAuthorsWithPaginationUseCase,
    private val searchBooksWithPaginationUseCase: SearchBooksWithPaginationUseCase
) : ViewModel() {
    private val _mutableLiveDataBookPopularity =
        MutableLiveData<BasicState<List<BookList>>>(BasicState.DefaultState())
    private val _mutableLiveDataTheBestAuthors =
        MutableLiveData<BasicState<List<AuthorList>>>(BasicState.DefaultState())
    val liveDataBookPopularity get() = _mutableLiveDataBookPopularity
    val liveDataTheBestAuthors get() = _mutableLiveDataTheBestAuthors

    fun getPopularBooks() {
        viewModelScope.launch {
            _mutableLiveDataBookPopularity.value = BasicState.LoadingState()
            var state = searchBooksUseCase.execute(
                numberPage = 0,
                sizePage = SIZE_PAGE,
                word = null,
                sort = SORT_POPULARITY,
                startRating = null,
                finishRating = null,
                tags = null,
                genres = null
            )
            if (state is BasicState.EmptyState) {
                state = BasicState.ErrorState()
            }
            _mutableLiveDataBookPopularity.value = state
        }
    }

    fun getBestAuthors() {
        viewModelScope.launch {
            _mutableLiveDataTheBestAuthors.value = BasicState.LoadingState()
            var state = searchAuthorsUseCase.execute(
                numberPage = 0,
                sizePage = SIZE_PAGE,
                word = null,
                sort = SORT_RATING,
                startRating = null,
                finishRating = null,
            )
            if (state is BasicState.EmptyState) {
                state = BasicState.ErrorState()
            }
            _mutableLiveDataTheBestAuthors.value = state
        }
    }

    fun getAuthorResult(
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>> {
        return searchAuthorsWithPaginationUseCase.execute(
            sizePage = SIZE_PAGE,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
            .cachedIn(viewModelScope)
    }

    fun getBookResult(
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        genres: String?,
        tags: String?
    ): Flow<PagingData<BookList>> {
        return searchBooksWithPaginationUseCase.execute(
            sizePage = SIZE_PAGE,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating,
            genres = genres,
            tags = tags
        ).cachedIn(viewModelScope)
    }


   companion object {
        const val SIZE_PAGE = 5
        const val SORT_POPULARITY = "popularity"
        const val SORT_RATING = "rating"
    }
}
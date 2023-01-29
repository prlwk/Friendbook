package com.src.book.presentation.main.main_page.viewModel

import androidx.paging.PagingData
import com.src.book.domain.author.AuthorList
import com.src.book.domain.model.book.BookList
import kotlinx.coroutines.flow.Flow

class Result(
    val authors: Flow<PagingData<AuthorList>>?,
    val books: Flow<PagingData<BookList>>?
)
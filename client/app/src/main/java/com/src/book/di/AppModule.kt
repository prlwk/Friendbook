package com.src.book.di

import android.content.Context
import com.src.book.domain.usecase.GetAuthorUseCase
import com.src.book.domain.usecase.GetBookByIdUseCase
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModelFactory
import com.src.book.presentation.book.main_page.viewModel.BookViewModel
import com.src.book.presentation.main.list_of_books.viewModel.ListOfBooksViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule(var context: Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideAuthorViewModelFactory(getAuthorUseCase: GetAuthorUseCase): AuthorViewModelFactory {
        return AuthorViewModelFactory(getAuthorUseCase = getAuthorUseCase)
    }

    @Provides
    fun provideListOfBooksViewModelFactory(getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase): ListOfBooksViewModelFactory {
        return ListOfBooksViewModelFactory(getBooksByAuthorIdUseCase = getBooksByAuthorIdUseCase)
    }

    @Provides
    fun provideBookViewModelFactory(getBookByIdUseCase: GetBookByIdUseCase): BookViewModel {
        return BookViewModel(getBookByIdUseCase = getBookByIdUseCase)
    }
}
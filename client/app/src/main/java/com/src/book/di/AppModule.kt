package com.src.book.di

import android.content.Context
import com.src.book.domain.usecase.author.GetAuthorUseCase
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModelFactory
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModelFactory
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

//    @Provides
//    fun provideBookViewModelFactory(getBookByIdUseCase: GetBookByIdUseCase): BookViewModelFactory {
//        return BookViewModelFactory(getBookByIdUseCase = getBookByIdUseCase)
//    }
}
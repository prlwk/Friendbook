package com.src.book.di

import android.content.Context
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.usecase.GetAuthorUseCase
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}
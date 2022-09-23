package com.src.book.di

import com.src.book.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
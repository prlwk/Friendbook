package com.src.book.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(var context:Context) {
    @Provides
    fun provideContext(): Context {
        return context
    }
}
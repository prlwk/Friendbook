package com.src.book.di

import com.src.book.presentation.MainActivity
import com.src.book.presentation.registration.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, MapperModule::class, NetworkModule::class,LocalModule::class, DataModule::class, DomainModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(loginActivity:LoginActivity)
}
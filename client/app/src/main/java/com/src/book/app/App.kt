package com.src.book.app

import android.app.Application
import com.src.book.di.AppComponent
import com.src.book.di.AppModule
import com.src.book.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .build()
    }
}
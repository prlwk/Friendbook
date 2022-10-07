package com.src.book.di

import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.remote.dataSource.author.AuthorDataSourceImpl
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.book.BookDataSourceImpl
import com.src.book.data.remote.service.AuthorService
import com.src.book.data.remote.service.BookService
import com.src.book.data.remote.service.ReviewService
import com.src.book.data.remote.service.UserService
import com.src.book.utlis.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideBookService(retrofit: Retrofit): BookService {
        return retrofit.create(BookService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthorService(retrofit: Retrofit): AuthorService {
        return retrofit.create(AuthorService::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewService(retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthorDataSource(
        authorService: AuthorService
    ): AuthorDataSource {
        return AuthorDataSourceImpl(authorService = authorService)
    }

    @Singleton
    @Provides
    fun provideBookDataSource(
        bookService: BookService
    ): BookDataSource {
        return BookDataSourceImpl(bookService = bookService)
    }
}


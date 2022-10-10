package com.src.book.di

import com.src.book.data.remote.model.author.author.AuthorMapper
import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.review.reviewBook.ReviewBookMapper
import com.src.book.data.remote.model.tag.TagMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {
    @Singleton
    @Provides
    fun provideGenreMapper(): GenreMapper {
        return GenreMapper()
    }

    @Singleton
    @Provides
    fun provideTagMapper(): TagMapper {
        return TagMapper()
    }

    @Singleton
    @Provides
    fun provideReviewBookMapper(): ReviewBookMapper {
        return ReviewBookMapper()
    }

    @Singleton
    @Provides
    fun provideAuthorBookMapper(): AuthorBookMapper {
        return AuthorBookMapper()
    }

    @Singleton
    @Provides
    fun provideBookAuthorMapper(): BookAuthorMapper {
        return BookAuthorMapper()
    }

    @Singleton
    @Provides
    fun providesBookMapper(
        tagMapper: TagMapper,
        genreMapper: GenreMapper,
        authorBookMapper: AuthorBookMapper,
        reviewBookMapper: ReviewBookMapper
    ): BookMapper {
        return BookMapper(
            tagMapper = tagMapper,
            genreMapper = genreMapper,
            authorBookMapper = authorBookMapper,
            reviewBookMapper = reviewBookMapper
        )
    }

    @Singleton
    @Provides
    fun provideAuthorMapper(
        bookAuthorMapper: BookAuthorMapper
    ): AuthorMapper {
        return AuthorMapper(bookAuthorMapper = bookAuthorMapper)
    }
}
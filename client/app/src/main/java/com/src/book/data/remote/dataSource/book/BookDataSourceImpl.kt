package com.src.book.data.remote.dataSource.book

import com.src.book.data.remote.model.author.AuthorBookResponse
import com.src.book.data.remote.model.book.BookResponse
import com.src.book.data.remote.model.GenreResponse
import com.src.book.data.remote.model.ReviewBookResponse
import com.src.book.data.remote.model.TagResponse
import com.src.book.data.remote.service.BookService
import com.src.book.domain.model.*

class BookDataSourceImpl(private val bookService: BookService) : BookDataSource {
    override suspend fun loadBooksByAuthorId(id: Long): List<Book>? {
        return try {
            val booksResponse = bookService.getAllBooksByAuthorId(id)
            booksResponse.body()?.map { responseBookToModel(it) }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun loadBookById(id: Long): Book? {
        return try {
            val bookResponse = bookService.getBookById(id)
            bookResponse.body()?.let { responseBookToModel(it) }
        } catch (exception: Exception) {
            null
        }
    }

    private suspend fun responseGenreToModel(genreResponse: GenreResponse) =
        Genre(
            id = genreResponse.id,
            name = genreResponse.name
        )

    private suspend fun responseTagToModel(tagResponse: TagResponse) =
        Tag(
            id = tagResponse.id,
            name = tagResponse.name
        )

    private suspend fun responseAuthorBookToModel(authorBookResponse: AuthorBookResponse) =
        AuthorBook(
            id = authorBookResponse.id,
            name = authorBookResponse.name
        )

    private suspend fun responseReviewToModel(reviewResponse: ReviewBookResponse) =
        Review(
            id = reviewResponse.id,
            username = reviewResponse.username,
            photoSrc = reviewResponse.photoSrc,
            reviewText = reviewResponse.reviewText,
            rating = reviewResponse.rating
        )

    private suspend fun responseBookToModel(bookResponse: BookResponse) =
        Book(
            id = bookResponse.id,
            name = bookResponse.name,
            rating = bookResponse.rating,
            linkCover = bookResponse.linkCover,
            year = bookResponse.year,
            genres = bookResponse.genres?.map { responseGenreToModel(it) },
            authors = bookResponse.authors?.map { responseAuthorBookToModel(it) },
            reviews = bookResponse.reviews?.map { responseReviewToModel(it) },
            description = bookResponse.description,
            tags = bookResponse.tags?.map { responseTagToModel(it) }
        )
}
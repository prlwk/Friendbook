package com.src.book.data.remote.dataSource.book

import android.net.Uri
import com.src.book.data.remote.model.author.AuthorBookResponse
import com.src.book.data.remote.model.book.BookResponse
import com.src.book.data.remote.model.GenreResponse
import com.src.book.data.remote.service.BookService
import com.src.book.domain.model.AuthorBook
import com.src.book.domain.model.Book
import com.src.book.domain.model.Genre
import com.src.book.utlis.BOOK_SERVICE_BASE_URL

class BookDataSourceImpl(private val bookService: BookService) : BookDataSource {
    override suspend fun loadBooksByAuthorId(id: Long): List<Book>? {
        val url = Uri.parse("${BOOK_SERVICE_BASE_URL}book/by-author-id/${id}")
        val booksResponse = bookService.getAllBooksByAuthorId(url)
        return booksResponse.map { responseBookToModel(it) }
    }

    private suspend fun responseGenreToModel(genreResponse: GenreResponse) =
        Genre(
            id = genreResponse.id,
            name = genreResponse.name
        )

    private suspend fun responseAuthorBookToModel(authorBookResponse: AuthorBookResponse) =
        AuthorBook(
            id = authorBookResponse.id,
            name = authorBookResponse.name
        )

    private suspend fun responseBookToModel(bookResponse: BookResponse) =
        Book(
            id = bookResponse.id,
            name = bookResponse.name,
            rating = bookResponse.rating,
            linkCover = bookResponse.linkCover,
            year = bookResponse.year,
            genres = bookResponse.genres.map { responseGenreToModel(it) },
            authors = bookResponse.authors.map { responseAuthorBookToModel(it) }
        )
}
package com.src.book

import com.src.book.data.remote.model.author.author.AuthorResponse
import com.src.book.data.remote.model.author.authorBook.AuthorBookResponse
import com.src.book.data.remote.model.book.book.BookResponse
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorResponse
import com.src.book.data.remote.model.genre.GenreResponse
import com.src.book.data.remote.model.review.reviewBook.ReviewBookResponse
import com.src.book.data.remote.model.tag.TagResponse

class TestModelsResponseGenerator {
    //author
    fun generateAuthorResponseModel() = AuthorResponse(
        id = ID,
        name = AUTHOR_NAME,
        yearsLife = AUTHOR_YEARS_LIFE,
        rating = RATING,
        photoSrc = null,
        biography = AUTHOR_BIOGRAPHY,
        books = listOf(generateBookAuthorResponseModel())
    )

    fun generateAuthorBookResponseModel() = AuthorBookResponse(
        id = ID,
        name = AUTHOR_NAME
    )

    //book
    fun generateBookResponseModel() = BookResponse(
        id = ID,
        name = BOOK_NAME,
        rating = RATING,
        linkCover = null,
        year = BOOK_YEAR,
        genres = listOf(generateGenreResponseModel()),
        authors = listOf(generateAuthorBookResponseModel()),
        reviews = listOf(generateReviewBookResponseModel()),
        description = BOOK_DESCRIPTION,
        tags = listOf(generateTagResponseModel())
    )

    fun generateBookAuthorResponseModel() = BookAuthorResponse(
        id = ID,
        rating = RATING,
        linkCover = null
    )

    //review
    fun generateReviewBookResponseModel() = ReviewBookResponse(
        id = ID,
        username = REVIEW_USER,
        photoSrc = null,
        reviewText = REVIEW_TEXT,
        rating = REVIEW_RATING
    )

    //tag
    fun generateTagResponseModel() = TagResponse(
        id = ID,
        name = TAG_NAME
    )

    //genre
    fun generateGenreResponseModel() = GenreResponse(
        id = ID,
        name = GENRE_NAME
    )
}
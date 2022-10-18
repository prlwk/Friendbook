package com.src.book

import com.src.book.domain.model.*

class TestModelsGenerator {
    fun generateAuthorModel(): Author {
        return Author(
            id = 1,
            name = "Author",
            yearsLife = "1980-2010",
            rating = 3.6,
            photoSrc = null,
            biography = "biography",
            books = null
        )
    }

    fun generateBookModel() = Book(
        id = 1,
        name = "book",
        rating = 1.2,
        linkCover = null,
        year = "1990",
        genres = null,
        authors = null,
        reviews = null,
        description = "description",
        tags = null
    )

    fun generateListOfBooksModel() = listOf(generateBookModel())
}
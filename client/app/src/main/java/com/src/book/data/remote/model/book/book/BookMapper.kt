package com.src.book.data.remote.model.book.book

import com.src.book.data.remote.Mapper
import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.review.reviewBook.ReviewBookMapper
import com.src.book.data.remote.model.tag.TagMapper
import com.src.book.domain.model.book.Book

class BookMapper(
    private val tagMapper: TagMapper,
    private val reviewBookMapper: ReviewBookMapper,
    private val genreMapper: GenreMapper,
    private val authorBookMapper: AuthorBookMapper
) : Mapper<Book, BookResponse> {
    override suspend fun mapFromResponseToModel(data: BookResponse): Book {
        return Book(
            id = data.id,
            name = data.name,
            rating = data.rating,
            linkCover = data.linkCover,
            year = data.year,
            genres = data.genres.map { genreMapper.mapFromResponseToModel(it) },
            authors = data.authors.map { authorBookMapper.mapFromResponseToModel(it) },
            reviews = data.reviews.map { reviewBookMapper.mapFromResponseToModel(it) },
            description = data.description,
            tags = data.tags?.map { tagMapper.mapFromResponseToModel(it) },
            isWantToRead = data.isWantToRead,
            grade = data.grade
        )
    }
}
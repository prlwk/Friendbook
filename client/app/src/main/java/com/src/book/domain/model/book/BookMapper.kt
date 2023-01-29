package com.src.book.domain.model.book

class BookMapper {
    fun mapBookListToBookAuthor(bookList: BookList) = BookAuthor(
        id = bookList.id,
        rating = bookList.rating,
        linkCover = bookList.linkCover
    )
}
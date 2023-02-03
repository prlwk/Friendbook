package com.src.book.domain.model.book.genre

sealed class GenreWithTitle {
    class Title(val title: String) : GenreWithTitle()

    class Genre(val genreWithCheck: GenreWithCheck) : GenreWithTitle()
}
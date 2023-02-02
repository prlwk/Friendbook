package com.src.book.domain.model.book.genre

import com.src.book.domain.model.Genre

data class GenreWithCheck(
    val genre: Genre,
    var isSelected: Boolean = false
)
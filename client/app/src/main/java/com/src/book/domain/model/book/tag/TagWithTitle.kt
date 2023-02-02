package com.src.book.domain.model.book.tag

sealed class TagWithTitle {
    class Title(val title: String) : TagWithTitle()

    class Tag(val tagWithCheck: TagWithCheck) : TagWithTitle()
}
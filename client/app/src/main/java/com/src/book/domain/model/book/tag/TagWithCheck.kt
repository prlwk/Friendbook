package com.src.book.domain.model.book.tag

import com.src.book.domain.model.Tag

data class TagWithCheck(
    val tag: Tag,
    var isSelected: Boolean = false
)
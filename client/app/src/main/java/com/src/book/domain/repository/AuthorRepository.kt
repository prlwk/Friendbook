package com.src.book.domain.repository

import com.src.book.domain.model.Author
import com.src.book.domain.utils.BasicState

interface AuthorRepository {
    suspend fun getAuthorById(id: Long): BasicState<Author>
}
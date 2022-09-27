package com.src.book.domain.repository

import com.src.book.domain.model.Author

interface AuthorRepository {
    suspend fun getAuthorById(id: Long): Author?
}
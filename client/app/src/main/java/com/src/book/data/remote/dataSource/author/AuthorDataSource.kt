package com.src.book.data.remote.dataSource.author

import com.src.book.domain.model.Author

interface AuthorDataSource {
    suspend fun loadAuthorById(id:Long): Author?
}
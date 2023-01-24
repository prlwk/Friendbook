package com.src.book.data.remote.dataSource.author

import com.src.book.domain.model.Author
import com.src.book.domain.utils.BasicState

interface AuthorDataSource {
    suspend fun loadAuthorById(id:Long): BasicState<Author>
}
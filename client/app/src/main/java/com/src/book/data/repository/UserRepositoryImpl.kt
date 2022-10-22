package com.src.book.data.repository

import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    override suspend fun changePassword(password: String): BasicState =
        withContext(Dispatchers.IO) {
            return@withContext userDataSource.changePassword(password)
        }
}
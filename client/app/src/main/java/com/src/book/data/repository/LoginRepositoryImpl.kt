package com.src.book.data.repository

import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepositoryImpl(private val loginDataSource: LoginDataSource) : LoginRepository {
    override suspend fun signIn(data: Map<String, String>) = withContext(Dispatchers.IO) {
       return@withContext loginDataSource.signIn(data)
    }
}
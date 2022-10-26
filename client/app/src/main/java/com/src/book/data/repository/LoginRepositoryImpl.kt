package com.src.book.data.repository

import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.domain.model.user.Login
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.CodeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepositoryImpl(private val loginDataSource: LoginDataSource) : LoginRepository {
    override suspend fun signIn(data: Login) = withContext(Dispatchers.IO) {
        return@withContext loginDataSource.signIn(data)
    }

    override suspend fun checkEmailExists(email: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext loginDataSource.checkEmailExists(email)
    }

    override suspend fun checkRecoveryCode(code: String, email: String): CodeState =
        withContext(Dispatchers.IO) {
            return@withContext loginDataSource.checkRecoveryCode(code = code, email = email)
        }

    override suspend fun sendCodeForRecoveryPassword(email: String): CodeState =
        withContext(Dispatchers.IO) {
            return@withContext loginDataSource.sendCodeForRecoveryPassword(email)
        }
}
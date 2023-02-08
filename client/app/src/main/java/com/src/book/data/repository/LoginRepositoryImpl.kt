package com.src.book.data.repository

import com.src.book.data.local.repository.LocalUserRepository
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.domain.model.user.Login
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LoginRepositoryImpl(
    private val loginDataSource: LoginDataSource,
    private val userLocalRepository: LocalUserRepository
) : LoginRepository {
    override suspend fun signIn(data: Login) = withContext(Dispatchers.IO) {
        return@withContext loginDataSource.signIn(data)
    }

    override suspend fun checkEmailExists(email: String): BasicState<Boolean> =
        withContext(Dispatchers.IO) {
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

    override suspend fun registration(data: String, file: File?): RegistrationState =
        withContext(Dispatchers.IO) {
            return@withContext loginDataSource.registration(data, file)
        }

    override suspend fun checkRecoveryCodeForAccountConfirmations(
        code: String,
        email: String
    ): CodeState = withContext(Dispatchers.IO) {
        return@withContext loginDataSource.checkRecoveryCodeForAccountConfirmations(code, email)
    }

    override suspend fun sendCodeForAccountConfirmations(): BasicState<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext loginDataSource.sendCodeForAccountConfirmations()
        }

    override suspend fun setIsActiveAndClearSession() = withContext(Dispatchers.IO) {
        userLocalRepository.setIsActiveAndClearSession()
    }
}
package com.src.book.data.repository

import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): ChangePasswordState =
        withContext(Dispatchers.IO) {
            return@withContext userDataSource.changePassword(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
        }

    override suspend fun logout(): BasicState = withContext(Dispatchers.IO) {
        return@withContext userDataSource.logout()
    }
}
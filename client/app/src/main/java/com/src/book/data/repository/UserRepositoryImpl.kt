package com.src.book.data.repository

import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.EditProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {
    override suspend fun changePassword(
        oldPassword: String?,
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

    override suspend fun editProfile(data: String, file: File?): EditProfileState =
        withContext(Dispatchers.IO) {
            return@withContext userDataSource.editProfile(data, file)
        }

    override suspend fun getProfile(): BasicState = withContext(Dispatchers.IO) {
        return@withContext userDataSource.getProfile()
    }
}
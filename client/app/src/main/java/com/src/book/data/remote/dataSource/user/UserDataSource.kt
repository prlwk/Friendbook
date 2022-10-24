package com.src.book.data.remote.dataSource.user

import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState

interface UserDataSource {
    suspend fun changePassword(oldPassword: String, newPassword: String): ChangePasswordState
    suspend fun logout(): BasicState
}
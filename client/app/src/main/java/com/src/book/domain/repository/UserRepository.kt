package com.src.book.domain.repository

import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState

interface UserRepository {
    suspend fun changePassword(oldPassword: String, newPassword: String): ChangePasswordState
    suspend fun logout():BasicState
}
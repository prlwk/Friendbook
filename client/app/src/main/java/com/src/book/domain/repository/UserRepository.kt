package com.src.book.domain.repository

import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.SendFriendRequestState

interface UserRepository {
    suspend fun changePassword(oldPassword: String, newPassword: String): ChangePasswordState
    suspend fun logout(): BasicState
    suspend fun sendFriendRequest(login: String): SendFriendRequestState
}
package com.src.book.domain.repository

import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.EditProfileState
import com.src.book.domain.utils.SendFriendRequestState
import java.io.File

interface UserRepository {
    suspend fun changePassword(oldPassword: String?, newPassword: String): ChangePasswordState
    suspend fun logout(): BasicState
    suspend fun editProfile(data: String, file: File?): EditProfileState
    suspend fun getProfile(): BasicState
}
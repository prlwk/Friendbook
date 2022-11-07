package com.src.book.data.remote.dataSource.user

import com.src.book.domain.utils.*
import java.io.File

interface UserDataSource {
    suspend fun changePassword(oldPassword: String?, newPassword: String): ChangePasswordState
    suspend fun logout(): BasicState
    suspend fun editProfile(data: String, file: File?): EditProfileState
}
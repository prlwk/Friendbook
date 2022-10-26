package com.src.book.domain.usecase.user

import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.ChangePasswordState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordUseCase(private val userRepository: UserRepository) {
    suspend fun execute(oldPassword: String?, newPassword: String): ChangePasswordState =
        withContext(Dispatchers.IO) {
            return@withContext userRepository.changePassword(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
        }
}
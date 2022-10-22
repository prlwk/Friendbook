package com.src.book.domain.usecase

import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordUseCase(private val userRepository: UserRepository) {
    suspend fun execute(password: String): BasicState = withContext(Dispatchers.IO) {
        return@withContext userRepository.changePassword(password)
    }
}
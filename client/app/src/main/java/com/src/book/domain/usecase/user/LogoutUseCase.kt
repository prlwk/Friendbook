package com.src.book.domain.usecase.user

import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogoutUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): BasicState = withContext(Dispatchers.IO) {
        return@withContext userRepository.logout()
    }
}
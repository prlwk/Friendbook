package com.src.book.domain.usecase.user

import com.src.book.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendFriendRequestUseCase(private val userRepository: UserRepository) {
   suspend fun execute(login: String) = withContext(Dispatchers.IO) {
        return@withContext userRepository.sendFriendRequest(login)

    }
}
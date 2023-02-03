package com.src.book.domain.usecase.friend

import com.src.book.domain.repository.FriendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendFriendRequestUseCase(private val friendRepository: FriendRepository) {
   suspend fun execute(login: String) = withContext(Dispatchers.IO) {
        return@withContext friendRepository.sendFriendRequest(login)

    }
}
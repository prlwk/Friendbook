package com.src.book.domain.usecase.friend

import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetOutgoingRequestsUseCase(private val friendRepository: FriendRepository) {
    suspend fun execute():BasicState<List<FriendRequest>> = withContext(Dispatchers.IO) {
        return@withContext friendRepository.getOutgoingRequests()
    }
}
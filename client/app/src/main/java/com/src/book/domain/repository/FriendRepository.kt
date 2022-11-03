package com.src.book.domain.repository

import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.SendFriendRequestState

interface FriendRepository {
    suspend fun getIncomingRequests(): BasicState
    suspend fun submitFriendRequest(id: Long): BasicState
    suspend fun rejectIncomingFriendRequest(id: Long): BasicState
    suspend fun getOutgoingRequests(): BasicState
    suspend fun rejectOutgoingFriendRequest(id: Long): BasicState
    suspend fun sendFriendRequest(login: String): SendFriendRequestState
    suspend fun getFriends():BasicState
}
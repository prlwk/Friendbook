package com.src.book.domain.repository

import com.src.book.domain.utils.BasicState

interface FriendRepository {
    suspend fun getIncomingRequests(): BasicState
    suspend fun submitFriendRequest(id: Long): BasicState
    suspend fun rejectIncomingFriendRequest(id: Long): BasicState
    suspend fun getOutgoingRequests(): BasicState
    suspend fun rejectOutgoingFriendRequest(id: Long): BasicState
}
package com.src.book.data.remote.dataSource.friend

import com.src.book.domain.utils.BasicState

interface FriendDataSource {
    suspend fun getIncomingRequests(): BasicState
    suspend fun submitFriendRequest(id: Long): BasicState
    suspend fun rejectIncomingFriendRequest(id: Long): BasicState
    suspend fun getOutgoingRequests(): BasicState
    suspend fun rejectOutgoingFriendRequest(id: Long): BasicState
}
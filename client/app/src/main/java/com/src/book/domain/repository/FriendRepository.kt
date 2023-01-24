package com.src.book.domain.repository

import com.src.book.domain.model.Friend
import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.SendFriendRequestState

interface FriendRepository {
    suspend fun getIncomingRequests(): BasicState<List<FriendRequest>>
    suspend fun submitFriendRequest(id: Long): BasicState<Unit>
    suspend fun rejectIncomingFriendRequest(id: Long): BasicState<Unit>
    suspend fun getOutgoingRequests(): BasicState<List<FriendRequest>>
    suspend fun rejectOutgoingFriendRequest(id: Long): BasicState<Unit>
    suspend fun sendFriendRequest(login: String): SendFriendRequestState
    suspend fun getFriends(): BasicState<List<Friend>>
    suspend fun getIncomingRequestsCount(): BasicState<Int>
    suspend fun removeFriend(friendId: Long): BasicState<Unit>
}
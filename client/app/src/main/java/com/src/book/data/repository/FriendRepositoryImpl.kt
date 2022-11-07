package com.src.book.data.repository

import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.SendFriendRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRepositoryImpl(private val friendDataSource: FriendDataSource) : FriendRepository {
    override suspend fun getIncomingRequests(): BasicState = withContext(Dispatchers.IO) {
        return@withContext friendDataSource.getIncomingRequests()
    }

    override suspend fun submitFriendRequest(id: Long): BasicState = withContext(Dispatchers.IO) {
        return@withContext friendDataSource.submitFriendRequest(id)
    }

    override suspend fun rejectIncomingFriendRequest(id: Long): BasicState =
        withContext(Dispatchers.IO) {
            return@withContext friendDataSource.rejectIncomingFriendRequest(id)
        }

    override suspend fun getOutgoingRequests(): BasicState = withContext(Dispatchers.IO) {
        return@withContext friendDataSource.getOutgoingRequests()
    }

    override suspend fun rejectOutgoingFriendRequest(id: Long): BasicState =
        withContext(Dispatchers.IO) {
            return@withContext friendDataSource.rejectOutgoingFriendRequest(id)
        }

    override suspend fun sendFriendRequest(login: String): SendFriendRequestState =
        withContext(Dispatchers.IO) {
            return@withContext friendDataSource.sendFriendRequest(login)
        }

    override suspend fun getFriends(): BasicState = withContext(Dispatchers.IO) {
        return@withContext friendDataSource.getFriends()
    }
}
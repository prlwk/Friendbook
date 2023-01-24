package com.src.book.data.remote.dataSource.friend

import com.src.book.data.remote.model.friend.friend.FriendMapper
import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import com.src.book.data.remote.service.FriendService
import com.src.book.data.remote.utils.ALREADY_FRIENDS
import com.src.book.data.remote.utils.ErrorMessage
import com.src.book.domain.model.Friend
import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.SendFriendRequestState

class FriendDataSourceImpl(
    private val friendService: FriendService,
    private val friendRequestMapper: FriendRequestMapper,
    private val friendMapper: FriendMapper
) : FriendDataSource {
    override suspend fun getIncomingRequests(): BasicState<List<FriendRequest>> {
        val response = friendService.getIncomingRequests()
        if (response.isSuccessful) {
            val friendRequests =
                response.body()?.map { friendRequestMapper.mapFromResponseToModel(it) }
            if (friendRequests != null) {
                return BasicState.SuccessState(friendRequests)
            }
        }
        return BasicState.ErrorState()
    }

    override suspend fun submitFriendRequest(id: Long): BasicState<Unit> {
        val response = friendService.submitFriendRequest(id)
        return if (response.isSuccessful) {
            BasicState.SuccessState(Unit)
        } else {
            BasicState.ErrorState()
        }
    }

    override suspend fun rejectIncomingFriendRequest(id: Long): BasicState<Unit> {
        val response = friendService.rejectIncomingFriendRequest(id)
        return if (response.isSuccessful) {
            BasicState.SuccessState(Unit)
        } else {
            BasicState.ErrorState()
        }
    }

    override suspend fun getOutgoingRequests(): BasicState<List<FriendRequest>> {
        val response = friendService.getOutgoingRequests()
        if (response.isSuccessful) {
            val requests = response.body()?.map { friendRequestMapper.mapFromResponseToModel(it) }
            if (requests != null) {
                return BasicState.SuccessState(requests)
            }
        }
        return BasicState.ErrorState()
    }

    override suspend fun rejectOutgoingFriendRequest(id: Long): BasicState<Unit> {
        val response = friendService.rejectOutgoingFriendRequest(id)
        return if (response.isSuccessful) {
            BasicState.SuccessState(Unit)
        } else {
            BasicState.ErrorState()
        }
    }

    override suspend fun sendFriendRequest(login: String): SendFriendRequestState {
        val response = friendService.sendFriendRequest(login)
        if (response.isSuccessful) {
            return SendFriendRequestState.SuccessState
        }
        when (response.code()) {
            404 -> {
                return SendFriendRequestState.ErrorLoginState
            }
            409 -> {
                val errorMessage = ErrorMessage<Unit>()
                val message = errorMessage.getErrorMessage(response)
                if (message == ALREADY_FRIENDS) {
                    return SendFriendRequestState.FriendAlreadyExists
                }
                return SendFriendRequestState.SuchRequestAlreadyExists
            }
        }
        return SendFriendRequestState.ErrorState
    }

    override suspend fun getFriends(): BasicState<List<Friend>> {
        val response = friendService.getFriends()
        if (response.isSuccessful) {
            val friends = response.body()?.map { friendMapper.mapFromResponseToModel(it) }
            if (friends != null) {
                return BasicState.SuccessState(friends)
            }
        }
        return BasicState.ErrorState()
    }

    override suspend fun getIncomingRequestsCount(): BasicState<Int> {
        val response = friendService.getIncomingRequestsCount()
        if (response.isSuccessful) {
            if (response.body() != null) {
                val count = response.body()
                if (count != null) {
                    return BasicState.SuccessState(count)
                }
            }
        }
        return BasicState.ErrorState()
    }

    override suspend fun removeFriend(friendId: Long): BasicState<Unit> {
        val response = friendService.removeFriend(friendId)
        if (response.isSuccessful) {
            return BasicState.SuccessState(Unit)
        }
        return BasicState.ErrorState()
    }
}
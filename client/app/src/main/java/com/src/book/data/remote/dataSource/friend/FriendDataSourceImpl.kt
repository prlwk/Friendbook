package com.src.book.data.remote.dataSource.friend

import com.src.book.data.remote.model.friend.friend.FriendMapper
import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import com.src.book.data.remote.service.FriendService
import com.src.book.data.remote.utils.ALREADY_FRIENDS
import com.src.book.data.remote.utils.ErrorMessage
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.SendFriendRequestState

class FriendDataSourceImpl(
    private val friendService: FriendService,
    private val friendRequestMapper: FriendRequestMapper,
    private val friendMapper: FriendMapper
) : FriendDataSource {
    override suspend fun getIncomingRequests(): BasicState {
        val response = friendService.getIncomingRequests()
        if (response.isSuccessful) {
            return BasicState.SuccessStateWithResources(
                response.body()?.map { friendRequestMapper.mapFromResponseToModel(it) })
        }
        return BasicState.ErrorState
    }

    override suspend fun submitFriendRequest(id: Long): BasicState {
        val response = friendService.submitFriendRequest(id)
        return if (response.isSuccessful) {
            BasicState.SuccessState
        } else {
            BasicState.ErrorState
        }
    }

    override suspend fun rejectIncomingFriendRequest(id: Long): BasicState {
        val response = friendService.rejectIncomingFriendRequest(id)
        return if (response.isSuccessful) {
            BasicState.SuccessState
        } else {
            BasicState.ErrorState
        }
    }

    override suspend fun getOutgoingRequests(): BasicState {
        val response = friendService.getOutgoingRequests()
        if (response.isSuccessful) {
            return BasicState.SuccessStateWithResources(
                response.body()?.map { friendRequestMapper.mapFromResponseToModel(it) })
        }
        return BasicState.ErrorState
    }

    override suspend fun rejectOutgoingFriendRequest(id: Long): BasicState {
        val response = friendService.rejectOutgoingFriendRequest(id)
        return if (response.isSuccessful) {
            BasicState.SuccessState
        } else {
            BasicState.ErrorState
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

    override suspend fun getFriends(): BasicState {
        val response = friendService.getFriends()
        if (response.isSuccessful) {
            return BasicState.SuccessStateWithResources(
                response.body()!!.map { friendMapper.mapFromResponseToModel(it) })
        }
        return BasicState.ErrorState
    }
}
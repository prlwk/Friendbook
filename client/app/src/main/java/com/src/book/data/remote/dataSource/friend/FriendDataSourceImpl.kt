package com.src.book.data.remote.dataSource.friend

import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import com.src.book.data.remote.service.FriendService
import com.src.book.domain.utils.BasicState

class FriendDataSourceImpl(
    private val friendService: FriendService,
    private val friendRequestMapper: FriendRequestMapper
) : FriendDataSource {
    override suspend fun getIncomingRequests(): BasicState {
        val response = friendService.getIncomingRequests()
        Thread.sleep(5000)
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
        Thread.sleep(5000)
        if (response.isSuccessful) {
            return BasicState.SuccessStateWithResources(response.body()?.map { friendRequestMapper.mapFromResponseToModel(it) })
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
}
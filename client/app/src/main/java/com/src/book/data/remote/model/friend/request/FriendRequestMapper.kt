package com.src.book.data.remote.model.friend.request

import com.src.book.data.remote.Mapper
import com.src.book.domain.model.friend.FriendRequest.FriendRequest

class FriendRequestMapper : Mapper<FriendRequest, FriendRequestResponse> {
    override suspend fun mapFromResponseToModel(data: FriendRequestResponse): FriendRequest {
        return FriendRequest(
            id = data.id,
            name = data.name,
            login = data.login,
            imageUrl = data.imageUrl
        )
    }
}
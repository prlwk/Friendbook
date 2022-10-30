package com.src.book.presentation.friends.friends_requests.viewModel

import com.src.book.domain.model.friend.FriendRequest.FriendRequest

sealed class FriendRequestsState {
    class DefaultState(val friendsRequest: List<FriendRequest>) : FriendRequestsState()
    object ErrorState : FriendRequestsState()
    object EmptyState : FriendRequestsState()
}
package com.src.book.domain.utils

sealed class SendFriendRequestState {
    object SuccessState : SendFriendRequestState()
    object ErrorLoginState : SendFriendRequestState()
    object FriendAlreadyExists : SendFriendRequestState()
    object SuchRequestAlreadyExists : SendFriendRequestState()
    object ErrorState:SendFriendRequestState()
}
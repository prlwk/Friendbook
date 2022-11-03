package com.src.book.presentation.friends.friends_list

import com.src.book.domain.model.Friend

sealed class FriendsListState {
    class SuccessState(val friend: List<Friend>) : FriendsListState()
    object ErrorState : FriendsListState()
    object DefaultState : FriendsListState()
}
package com.src.book.presentation.friends.add_friends.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.friend.SendFriendRequestUseCase
import javax.inject.Inject

class AddFriendsViewModelFactory @Inject constructor(val sendFriendRequestUseCase: SendFriendRequestUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AddFriendsViewModel(sendFriendRequestUseCase = sendFriendRequestUseCase) as T
}
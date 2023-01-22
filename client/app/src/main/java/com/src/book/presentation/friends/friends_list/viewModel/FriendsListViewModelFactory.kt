package com.src.book.presentation.friends.friends_list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.friend.GetFriendsUseCase
import com.src.book.domain.usecase.friend.GetIncomingRequestsCountUseCase
import com.src.book.domain.usecase.friend.RemoveFriendUseCase
import javax.inject.Inject

class FriendsListViewModelFactory @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getIncomingRequestsCountUseCase: GetIncomingRequestsCountUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FriendsListViewModel(
            getFriendsUseCase = getFriendsUseCase,
            getIncomingRequestsCountUseCase = getIncomingRequestsCountUseCase,
            removeFriendUseCase = removeFriendUseCase
        ) as T
    }
}
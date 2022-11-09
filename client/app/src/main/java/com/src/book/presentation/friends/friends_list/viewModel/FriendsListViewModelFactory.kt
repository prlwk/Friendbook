package com.src.book.presentation.friends.friends_list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.friend.GetFriendsUseCase
import com.src.book.domain.usecase.friend.GetIncomingRequestsCountUseCase
import javax.inject.Inject

class FriendsListViewModelFactory @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getIncomingRequestsCountUseCase: GetIncomingRequestsCountUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FriendsListViewModel(
            getFriendsUseCase = getFriendsUseCase,
            getIncomingRequestsCountUseCase = getIncomingRequestsCountUseCase
        ) as T
    }
}
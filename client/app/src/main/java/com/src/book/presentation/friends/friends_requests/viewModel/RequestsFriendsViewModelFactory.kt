package com.src.book.presentation.friends.friends_requests.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.src.book.domain.usecase.friend.*
import javax.inject.Inject

class RequestsFriendsViewModelFactory @Inject constructor(
    private val getIncomingRequestsUseCase: GetIncomingRequestsUseCase,
    private val submitFriendRequestUseCase: SubmitFriendRequestUseCase,
    private val rejectIncomingFriendRequestUseCase: RejectIncomingFriendRequestUseCase,
    private val getOutgoingRequestUseCase: GetOutgoingRequestsUseCase,
    private val rejectOutgoingFriendRequestUseCase: RejectOutgoingFriendRequestUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RequestsFriendsViewModel(
            getIncomingRequestsUseCase = getIncomingRequestsUseCase,
            submitFriendRequestUseCase = submitFriendRequestUseCase,
            rejectIncomingFriendRequestUseCase = rejectIncomingFriendRequestUseCase,
            getOutgoingRequestUseCase = getOutgoingRequestUseCase,
            rejectOutgoingFriendRequestUseCase = rejectOutgoingFriendRequestUseCase
        ) as T
    }
}
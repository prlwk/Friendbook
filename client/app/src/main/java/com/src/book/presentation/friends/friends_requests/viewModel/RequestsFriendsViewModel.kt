package com.src.book.presentation.friends.friends_requests.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.domain.usecase.friend.*
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.launch

class RequestsFriendsViewModel(
    private val getIncomingRequestsUseCase: GetIncomingRequestsUseCase,
    private val submitFriendRequestUseCase: SubmitFriendRequestUseCase,
    private val rejectIncomingFriendRequestUseCase: RejectIncomingFriendRequestUseCase,
    private val getOutgoingRequestUseCase: GetOutgoingRequestsUseCase,
    private val rejectOutgoingFriendRequestUseCase: RejectOutgoingFriendRequestUseCase
) :
    ViewModel() {
    private val _mutableLiveDataIncomingRequests =
        MutableLiveData<FriendRequestsState>(FriendRequestsState.EmptyState)
    private val _mutableLiveDataOutgoingRequests =
        MutableLiveData<FriendRequestsState>(FriendRequestsState.EmptyState)
    private val _mutableLiveDataIncomingIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableLiveDataSubmitFriendState =
        MutableLiveData<BasicState>(BasicState.SuccessState)
    private val _mutableLiveDataIncomingRejectFriendState =
        MutableLiveData<BasicState>(BasicState.SuccessState)
    private val _mutableLiveDataOutgoingRejectFriendState =
        MutableLiveData<BasicState>(BasicState.SuccessState)

    val liveDataIncomingRequests get() = _mutableLiveDataIncomingRequests
    val liveDataOutgoingRequests get() = _mutableLiveDataOutgoingRequests
    val liveDataIncomingIsLoading get() = _mutableLiveDataIncomingIsLoading
    val liveDataSubmitFriendState get() = _mutableLiveDataSubmitFriendState
    val liveDataIncomingRejectFriendState get() = _mutableLiveDataIncomingRejectFriendState
    val liveDataOutgoingRejectFriendState get() = _mutableLiveDataOutgoingRejectFriendState

    fun loadIncomingRequests() {
        viewModelScope.launch {
            _mutableLiveDataIncomingIsLoading.value = true
            val incomingRequests = getIncomingRequestsUseCase.execute()
            if (incomingRequests is BasicState.SuccessStateWithResources<*>) {
                _mutableLiveDataIncomingRequests.value =
                    FriendRequestsState.DefaultState(incomingRequests.data as List<FriendRequest>)
            } else {
                _mutableLiveDataIncomingRequests.value = FriendRequestsState.ErrorState
            }
            _mutableLiveDataIncomingIsLoading.value = false
        }
    }

    fun loadOutgoingRequests() {
        viewModelScope.launch {
            _mutableLiveDataIncomingIsLoading.value = true
            val outgoingRequests = getOutgoingRequestUseCase.execute()
            if (outgoingRequests is BasicState.SuccessStateWithResources<*>) {
                _mutableLiveDataOutgoingRequests.value =
                    FriendRequestsState.DefaultState(outgoingRequests.data as List<FriendRequest>)
            } else {
                _mutableLiveDataOutgoingRequests.value = FriendRequestsState.ErrorState
            }
            _mutableLiveDataIncomingIsLoading.value = false
        }
    }

    fun submitFriendRequest(id: Long) {
        viewModelScope.launch {
            liveDataSubmitFriendState.value = submitFriendRequestUseCase.execute(id)
        }
    }

    fun rejectIncomingFriendRequest(id: Long) {
        viewModelScope.launch {
            liveDataIncomingRejectFriendState.value = rejectIncomingFriendRequestUseCase.execute(id)
        }
    }

    fun rejectOutgoingFriendRequest(id: Long) {
        viewModelScope.launch {
            liveDataOutgoingRejectFriendState.value = rejectOutgoingFriendRequestUseCase.execute(id)
        }
    }
}
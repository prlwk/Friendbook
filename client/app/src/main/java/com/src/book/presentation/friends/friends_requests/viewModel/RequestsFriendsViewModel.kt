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
        MutableLiveData<BasicState<List<FriendRequest>>>(BasicState.DefaultState())
    private val _mutableLiveDataOutgoingRequests =
        MutableLiveData<BasicState<List<FriendRequest>>>(BasicState.DefaultState())
    private val _mutableLiveDataIncomingIsLoading = MutableLiveData<Boolean>(false)
    private val _mutableLiveDataSubmitFriendState =
        MutableLiveData<BasicState<Unit>>(BasicState.DefaultState())
    private val _mutableLiveDataIncomingRejectFriendState =
        MutableLiveData<BasicState<Unit>>(BasicState.DefaultState())
    private val _mutableLiveDataOutgoingRejectFriendState =
        MutableLiveData<BasicState<Unit>>(BasicState.DefaultState())

    val liveDataIncomingRequests get() = _mutableLiveDataIncomingRequests
    val liveDataOutgoingRequests get() = _mutableLiveDataOutgoingRequests
    val liveDataIncomingIsLoading get() = _mutableLiveDataIncomingIsLoading
    val liveDataSubmitFriendState get() = _mutableLiveDataSubmitFriendState
    val liveDataIncomingRejectFriendState get() = _mutableLiveDataIncomingRejectFriendState
    val liveDataOutgoingRejectFriendState get() = _mutableLiveDataOutgoingRejectFriendState

    fun loadIncomingRequests() {
        viewModelScope.launch {
            _mutableLiveDataIncomingIsLoading.value = true
            _mutableLiveDataIncomingRequests.value = getIncomingRequestsUseCase.execute()
            _mutableLiveDataIncomingIsLoading.value = false
        }
    }

    fun loadOutgoingRequests() {
        viewModelScope.launch {
            _mutableLiveDataIncomingIsLoading.value = true
            _mutableLiveDataOutgoingRequests.value = getOutgoingRequestUseCase.execute()
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
package com.src.book.presentation.friends.add_friends.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.usecase.user.SendFriendRequestUseCase
import com.src.book.domain.utils.SendFriendRequestState
import kotlinx.coroutines.launch

class AddFriendsViewModel(private val sendFriendRequestUseCase: SendFriendRequestUseCase) :
    ViewModel() {
    private val _mutableLiveDataState =
        MutableLiveData<SendFriendRequestState>(SendFriendRequestState.SuccessState)
    private val _mutableLiveDataIsLoading = MutableLiveData<Boolean>(false)
    val liveDataState get() = _mutableLiveDataState
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun sendFriendRequest(login: String) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataState.value = sendFriendRequestUseCase.execute(login)
            _mutableLiveDataIsLoading.value = false
        }
    }
}
package com.src.book.presentation.friends.friends_list.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.Friend
import com.src.book.domain.usecase.friend.GetFriendsUseCase
import com.src.book.domain.usecase.friend.GetIncomingRequestsCountUseCase
import com.src.book.domain.usecase.friend.RemoveFriendUseCase
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.launch

class FriendsListViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getIncomingRequestsCountUseCase: GetIncomingRequestsCountUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase
) : ViewModel() {
    private val _mutableLiveDataFriends =
        MutableLiveData<BasicState<List<Friend>>>(BasicState.DefaultState())
    private val _mutableLiveDataIncomingRequestsCount =
        MutableLiveData<BasicState<Int>>(BasicState.DefaultState())
    private val _mutableLiveDataRemoveFriend =
        MutableLiveData<BasicState<Unit>>(BasicState.DefaultState())

    val liveDataFriends get() = _mutableLiveDataFriends
    val liveDataIncomingRequestsCount get() = _mutableLiveDataIncomingRequestsCount
    val liveDataRemoveFriend get() = _mutableLiveDataRemoveFriend
    fun loadFriends() {
        viewModelScope.launch {
            _mutableLiveDataFriends.value = BasicState.LoadingState()
            _mutableLiveDataFriends.value = getFriendsUseCase.execute()
        }
    }

    fun loadIncomingRequestsCount() {
        viewModelScope.launch {
            _mutableLiveDataIncomingRequestsCount.value = BasicState.DefaultState()
            _mutableLiveDataIncomingRequestsCount.value = getIncomingRequestsCountUseCase.execute()

        }
    }

    fun removeFriend(friendId: Long) {
        viewModelScope.launch {
            _mutableLiveDataRemoveFriend.value = BasicState.DefaultState()
            _mutableLiveDataRemoveFriend.value = removeFriendUseCase.execute(friendId)
        }
    }
}
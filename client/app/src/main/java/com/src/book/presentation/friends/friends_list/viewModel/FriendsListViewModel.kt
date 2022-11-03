package com.src.book.presentation.friends.friends_list.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.Friend
import com.src.book.domain.usecase.friend.GetFriendsUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.friends.friends_list.FriendsListState
import kotlinx.coroutines.launch

class FriendsListViewModel(private val getFriendsUseCase: GetFriendsUseCase) : ViewModel() {
    private val _mutableLiveDataFriends =
        MutableLiveData<FriendsListState>(FriendsListState.DefaultState)
    private val _mutableLiveDataIsLoading = MutableLiveData(false)
    val liveDataFriends get() = _mutableLiveDataFriends
    val liveDataIsLoading get() = _mutableLiveDataIsLoading
    fun loadFriends() {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataFriends.value = FriendsListState.DefaultState
            val state = getFriendsUseCase.execute()
            if (state is BasicState.SuccessStateWithResources<*>) {
                _mutableLiveDataFriends.value =
                    FriendsListState.SuccessState((state as BasicState.SuccessStateWithResources<List<Friend>>).data)
            } else {
                _mutableLiveDataFriends.value = FriendsListState.ErrorState
            }
            _mutableLiveDataIsLoading.value = false
        }
    }
}
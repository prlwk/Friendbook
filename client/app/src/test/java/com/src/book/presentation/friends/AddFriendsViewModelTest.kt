package com.src.book.presentation.friends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.LOGIN
import com.src.book.domain.usecase.friend.SendFriendRequestUseCase
import com.src.book.domain.utils.SendFriendRequestState
import com.src.book.presentation.friends.add_friends.viewModel.AddFriendsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class AddFriendsViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var sendFriendRequestUseCase: SendFriendRequestUseCase

    private lateinit var addFriendsViewModel: AddFriendsViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        sendFriendRequestUseCase = mockk()
        addFriendsViewModel = AddFriendsViewModel(sendFriendRequestUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSendFriendRequestSuccessful() = runTest {
        coEvery { sendFriendRequestUseCase.execute(any()) } returns SendFriendRequestState.SuccessState
        addFriendsViewModel.sendFriendRequest(LOGIN)
        Assert.assertEquals(
            SendFriendRequestState.SuccessState, addFriendsViewModel.liveDataState.value
        )
    }

    @Test
    fun testSendFriendRequestSuchRequestAlreadyExists() = runTest {
        coEvery { sendFriendRequestUseCase.execute(any()) } returns SendFriendRequestState.SuchRequestAlreadyExists
        addFriendsViewModel.sendFriendRequest(LOGIN)
        Assert.assertEquals(
            SendFriendRequestState.SuchRequestAlreadyExists, addFriendsViewModel.liveDataState.value
        )
    }

    @Test
    fun testSendFriendRequestFriendAlreadyExists() = runTest {
        coEvery { sendFriendRequestUseCase.execute(any()) } returns SendFriendRequestState.FriendAlreadyExists
        addFriendsViewModel.sendFriendRequest(LOGIN)
        Assert.assertEquals(
            SendFriendRequestState.FriendAlreadyExists, addFriendsViewModel.liveDataState.value
        )
    }

    @Test
    fun testSendFriendRequestError() = runTest {
        coEvery { sendFriendRequestUseCase.execute(any()) } returns SendFriendRequestState.ErrorState
        addFriendsViewModel.sendFriendRequest(LOGIN)
        Assert.assertEquals(
            SendFriendRequestState.ErrorState, addFriendsViewModel.liveDataState.value
        )
    }
}
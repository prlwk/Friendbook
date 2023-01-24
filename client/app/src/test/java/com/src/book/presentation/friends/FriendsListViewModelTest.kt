package com.src.book.presentation.friends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.friend.GetFriendsUseCase
import com.src.book.domain.usecase.friend.GetIncomingRequestsCountUseCase
import com.src.book.domain.usecase.friend.RemoveFriendUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.friends.friends_list.viewModel.FriendsListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FriendsListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var getFriendsUseCase: GetFriendsUseCase
    private lateinit var getIncomingRequestsCountUseCase: GetIncomingRequestsCountUseCase
    private lateinit var removeFriendUseCase: RemoveFriendUseCase
    private lateinit var friendsListViewModel: FriendsListViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getFriendsUseCase = mockk()
        getIncomingRequestsCountUseCase = mockk()
        removeFriendUseCase = mockk()
        friendsListViewModel = FriendsListViewModel(
            getFriendsUseCase = getFriendsUseCase,
            getIncomingRequestsCountUseCase = getIncomingRequestsCountUseCase,
            removeFriendUseCase = removeFriendUseCase
        )
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetFriendsSuccessful() = runTest {
        val friendsModel = listOf(testModelsGenerator.generateFriendModel())
        coEvery { getFriendsUseCase.execute() } returns BasicState.SuccessState(
            friendsModel
        )
        friendsListViewModel.loadFriends()
        Assert.assertTrue(friendsListViewModel.liveDataFriends.value is BasicState.SuccessState)
        Assert.assertEquals(
            (friendsListViewModel.liveDataFriends.value as BasicState.SuccessState).data,
            friendsModel
        )
    }

    @Test
    fun testGetFriendsError() = runTest {
        coEvery { getFriendsUseCase.execute() } returns BasicState.ErrorState()
        friendsListViewModel.loadFriends()
        Assert.assertTrue(friendsListViewModel.liveDataFriends.value is BasicState.ErrorState)
    }

    @Test
    fun testGetIncomingRequestsCountSuccessful() = runTest {
        coEvery { getIncomingRequestsCountUseCase.execute() } returns BasicState.SuccessState(
            3
        )
        friendsListViewModel.loadIncomingRequestsCount()
        Assert.assertTrue(friendsListViewModel.liveDataIncomingRequestsCount.value is BasicState.SuccessState)
        Assert.assertEquals(
            (friendsListViewModel.liveDataIncomingRequestsCount.value as BasicState.SuccessState<*>).data,
            3
        )
    }

    @Test
    fun testGetIncomingRequestsCountError() = runTest {
        coEvery { getIncomingRequestsCountUseCase.execute() } returns BasicState.ErrorState()
        friendsListViewModel.loadIncomingRequestsCount()
        Assert.assertTrue(friendsListViewModel.liveDataIncomingRequestsCount.value is BasicState.ErrorState)
    }
}
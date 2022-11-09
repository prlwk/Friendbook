package com.src.book.presentation.friends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.friend.GetFriendsUseCase
import com.src.book.domain.usecase.friend.GetIncomingRequestsCountUseCase
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.friends.friends_list.FriendsListState
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
    private lateinit var friendsListViewModel: FriendsListViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getFriendsUseCase = mockk()
        getIncomingRequestsCountUseCase = mockk()
        friendsListViewModel = FriendsListViewModel(
            getFriendsUseCase = getFriendsUseCase,
            getIncomingRequestsCountUseCase = getIncomingRequestsCountUseCase
        )
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetFriendsSuccessful() = runTest {
        val friendsModel = listOf(testModelsGenerator.getFriendModel())
        coEvery { getFriendsUseCase.execute() } returns BasicState.SuccessStateWithResources(
            friendsModel
        )
        friendsListViewModel.loadFriends()
        Assert.assertTrue(friendsListViewModel.liveDataFriends.value is FriendsListState.SuccessState)
        Assert.assertEquals(
            (friendsListViewModel.liveDataFriends.value as FriendsListState.SuccessState).friend,
            friendsModel
        )
    }

    @Test
    fun testGetFriendsError() = runTest {
        coEvery { getFriendsUseCase.execute() } returns BasicState.ErrorState
        friendsListViewModel.loadFriends()
        Assert.assertTrue(friendsListViewModel.liveDataFriends.value is FriendsListState.ErrorState)
    }

    @Test
    fun testGetIncomingRequestsCountSuccessful() = runTest {
        coEvery { getIncomingRequestsCountUseCase.execute() } returns BasicState.SuccessStateWithResources(
            3
        )
        friendsListViewModel.loadIncomingRequestsCount()
        Assert.assertTrue(friendsListViewModel.liveDataIncomingRequestsCount.value is BasicState.SuccessStateWithResources<*>)
        Assert.assertEquals(
            (friendsListViewModel.liveDataIncomingRequestsCount.value as BasicState.SuccessStateWithResources<*>).data,
            3
        )
    }

    @Test
    fun testGetIncomingRequestsCountError() = runTest {
        coEvery { getIncomingRequestsCountUseCase.execute() } returns BasicState.ErrorState
        friendsListViewModel.loadIncomingRequestsCount()
        Assert.assertTrue(friendsListViewModel.liveDataIncomingRequestsCount.value is BasicState.ErrorState)
    }
}
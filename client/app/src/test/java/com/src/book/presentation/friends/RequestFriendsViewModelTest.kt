package com.src.book.presentation.friends

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.ID
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.friend.*
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.friends.friends_requests.viewModel.RequestsFriendsViewModel
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
class RequestFriendsViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var getIncomingRequestsUseCase: GetIncomingRequestsUseCase
    private lateinit var submitFriendRequestUseCase: SubmitFriendRequestUseCase
    private lateinit var rejectIncomingFriendRequestUseCase: RejectIncomingFriendRequestUseCase
    private lateinit var getOutgoingRequestUseCase: GetOutgoingRequestsUseCase
    private lateinit var rejectOutgoingFriendRequestUseCase: RejectOutgoingFriendRequestUseCase

    private lateinit var requestsFriendsViewModel: RequestsFriendsViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getIncomingRequestsUseCase = mockk()
        submitFriendRequestUseCase = mockk()
        rejectIncomingFriendRequestUseCase = mockk()
        getOutgoingRequestUseCase = mockk()
        rejectOutgoingFriendRequestUseCase = mockk()
        requestsFriendsViewModel = RequestsFriendsViewModel(
            getIncomingRequestsUseCase = getIncomingRequestsUseCase,
            submitFriendRequestUseCase = submitFriendRequestUseCase,
            rejectIncomingFriendRequestUseCase = rejectIncomingFriendRequestUseCase,
            getOutgoingRequestUseCase = getOutgoingRequestUseCase,
            rejectOutgoingFriendRequestUseCase = rejectOutgoingFriendRequestUseCase
        )
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadIncomingRequestsSuccessful() = runTest {
        val models = listOf(testModelsGenerator.generateFriendRequestModel())
        coEvery { getIncomingRequestsUseCase.execute() } returns BasicState.SuccessState(
            models
        )
        requestsFriendsViewModel.loadIncomingRequests()
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataIncomingRequests.value is BasicState.SuccessState
        )
        Assert.assertEquals(
            models,
            (requestsFriendsViewModel.liveDataIncomingRequests.value as BasicState.SuccessState).data
        )
    }

    @Test
    fun testLoadIncomingRequestsError() = runTest {
        coEvery { getIncomingRequestsUseCase.execute() } returns BasicState.ErrorState()
        requestsFriendsViewModel.loadIncomingRequests()
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataIncomingRequests.value is BasicState.ErrorState
        )
    }

    @Test
    fun testLoadOutgoingRequestsSuccessful() = runTest {
        val models = listOf(testModelsGenerator.generateFriendRequestModel())
        coEvery { getOutgoingRequestUseCase.execute() } returns BasicState.SuccessState(
            models
        )
        requestsFriendsViewModel.loadOutgoingRequests()
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataOutgoingRequests.value is BasicState.SuccessState
        )
        Assert.assertEquals(
            models,
            (requestsFriendsViewModel.liveDataOutgoingRequests.value as BasicState.SuccessState).data
        )
    }

    @Test
    fun testLoadOutgoingRequestsError() = runTest {
        coEvery { getOutgoingRequestUseCase.execute() } returns BasicState.ErrorState()
        requestsFriendsViewModel.loadOutgoingRequests()
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataOutgoingRequests.value is BasicState.ErrorState
        )
    }

    @Test
    fun testSubmitFriendRequestSuccessful() = runTest {
        coEvery { submitFriendRequestUseCase.execute(any()) } returns BasicState.SuccessState(Unit)
        requestsFriendsViewModel.submitFriendRequest(ID)
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataSubmitFriendState.value is BasicState.SuccessState
        )
    }

    @Test
    fun testSubmitFriendRequestError() = runTest {
        coEvery { submitFriendRequestUseCase.execute(any()) } returns BasicState.ErrorState()
        requestsFriendsViewModel.submitFriendRequest(ID)
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataSubmitFriendState.value is BasicState.ErrorState
        )
    }

    @Test
    fun testRejectIncomingFriendRequestSuccessful() = runTest {
        coEvery { rejectIncomingFriendRequestUseCase.execute(any()) } returns BasicState.SuccessState(Unit)
        requestsFriendsViewModel.rejectIncomingFriendRequest(ID)
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataIncomingRejectFriendState.value is BasicState.SuccessState
        )
    }

    @Test
    fun testRejectIncomingFriendRequestError() = runTest {
        coEvery { rejectIncomingFriendRequestUseCase.execute(any()) } returns BasicState.ErrorState()
        requestsFriendsViewModel.rejectIncomingFriendRequest(ID)
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataIncomingRejectFriendState.value is BasicState.ErrorState
        )
    }

    @Test
    fun testRejectOutgoingFriendRequestSuccessful() = runTest {
        coEvery { rejectOutgoingFriendRequestUseCase.execute(any()) } returns BasicState.SuccessState(Unit)
        requestsFriendsViewModel.rejectOutgoingFriendRequest(ID)
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataOutgoingRejectFriendState.value is BasicState.SuccessState
        )
    }

    @Test
    fun testRejectOutgoingFriendRequestError() = runTest {
        coEvery { rejectOutgoingFriendRequestUseCase.execute(any()) } returns BasicState.ErrorState()
        requestsFriendsViewModel.rejectOutgoingFriendRequest(ID)
        Assert.assertTrue(
            requestsFriendsViewModel.liveDataOutgoingRejectFriendState.value is BasicState.ErrorState
        )
    }
}
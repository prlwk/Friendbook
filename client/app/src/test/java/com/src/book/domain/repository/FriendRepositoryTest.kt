package com.src.book.domain.repository

import com.src.book.ID
import com.src.book.LOGIN
import com.src.book.TestModelsGenerator
import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.data.repository.FriendRepositoryImpl
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.SendFriendRequestState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class FriendRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var friendDataSource: FriendDataSource
    private lateinit var friendRepository: FriendRepository

    @Before
    fun setUp() {
        friendRepository = FriendRepositoryImpl(friendDataSource)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testSendFriendRequestSuccessful() = runTest {
        coEvery { friendDataSource.sendFriendRequest(any()) } returns SendFriendRequestState.SuccessState
        Assert.assertEquals(
            SendFriendRequestState.SuccessState,
            friendRepository.sendFriendRequest(LOGIN)
        )
    }

    @Test
    fun testSendFriendRequestSuchRequestAlreadyExists() = runTest {
        coEvery { friendDataSource.sendFriendRequest(any()) } returns SendFriendRequestState.SuchRequestAlreadyExists
        Assert.assertEquals(
            SendFriendRequestState.SuchRequestAlreadyExists,
            friendRepository.sendFriendRequest(LOGIN)
        )
    }

    @Test
    fun testSendFriendRequestFriendsAlreadyExists() = runTest {
        coEvery { friendDataSource.sendFriendRequest(any()) } returns SendFriendRequestState.FriendAlreadyExists
        Assert.assertEquals(
            SendFriendRequestState.FriendAlreadyExists,
            friendRepository.sendFriendRequest(LOGIN)
        )
    }

    @Test
    fun testSendFriendRequestError() = runTest {
        coEvery { friendDataSource.sendFriendRequest(any()) } returns SendFriendRequestState.ErrorState
        Assert.assertEquals(
            SendFriendRequestState.ErrorState,
            friendRepository.sendFriendRequest(LOGIN)
        )
    }

    @Test
    fun testGetIncomingRequestSuccessful() = runTest {
        val models = listOf(testModelsGenerator.generateFriendRequestModel())
        val successState = BasicState.SuccessStateWithResources(models)
        coEvery { friendDataSource.getIncomingRequests() } returns successState
        Assert.assertEquals(friendRepository.getIncomingRequests(), successState)
    }

    @Test
    fun testGetIncomingRequestError() = runTest {
        val state = BasicState.ErrorState
        coEvery { friendDataSource.getIncomingRequests() } returns state
        Assert.assertEquals(friendRepository.getIncomingRequests(), state)
    }

    @Test
    fun testGetOutgoingRequestSuccessful() = runTest {
        val models = listOf(testModelsGenerator.generateFriendRequestModel())
        val successState = BasicState.SuccessStateWithResources(models)
        coEvery { friendDataSource.getOutgoingRequests() } returns successState
        Assert.assertEquals(friendRepository.getOutgoingRequests(), successState)
    }

    @Test
    fun testGetOutgoingRequestError() = runTest {
        val state = BasicState.ErrorState
        coEvery { friendDataSource.getOutgoingRequests() } returns state
        Assert.assertEquals(friendRepository.getOutgoingRequests(), state)
    }

    @Test
    fun testSubmitFriendRequestSuccessful() = runTest {
        coEvery { friendDataSource.submitFriendRequest(any()) } returns BasicState.SuccessState
        Assert.assertTrue(friendRepository.submitFriendRequest(ID) is BasicState.SuccessState)
    }

    @Test
    fun testSubmitFriendRequestError() = runTest {
        coEvery { friendDataSource.submitFriendRequest(any()) } returns BasicState.ErrorState
        Assert.assertTrue(friendRepository.submitFriendRequest(ID) is BasicState.ErrorState)
    }

    @Test
    fun testRejectIncomingFriendRequestSuccessful() = runTest {
        coEvery { friendDataSource.rejectIncomingFriendRequest(any()) } returns BasicState.SuccessState
        Assert.assertTrue(friendRepository.rejectIncomingFriendRequest(ID) is BasicState.SuccessState)
    }

    @Test
    fun testRejectIncomingFriendRequestError() = runTest {
        coEvery { friendDataSource.rejectIncomingFriendRequest(any()) } returns BasicState.ErrorState
        Assert.assertTrue(friendRepository.rejectIncomingFriendRequest(ID) is BasicState.ErrorState)
    }

    @Test
    fun testRejectOutgoingFriendRequestSuccessful() = runTest {
        coEvery { friendDataSource.rejectOutgoingFriendRequest(any()) } returns BasicState.SuccessState
        Assert.assertTrue(friendRepository.rejectOutgoingFriendRequest(ID) is BasicState.SuccessState)
    }

    @Test
    fun testRejectOutgoingFriendRequestError() = runTest {
        coEvery { friendDataSource.rejectOutgoingFriendRequest(any()) } returns BasicState.ErrorState
        Assert.assertTrue(friendRepository.rejectOutgoingFriendRequest(ID) is BasicState.ErrorState)
    }

    @Test
    fun testGetFriendsSuccessful() = runTest {
        val friendsModel = listOf(testModelsGenerator.generateFriendModel())
        coEvery { friendDataSource.getFriends() } returns BasicState.SuccessStateWithResources(
            friendsModel
        )
        Assert.assertTrue(friendRepository.getFriends() is BasicState.SuccessStateWithResources<*>)
        Assert.assertEquals(
            (friendRepository.getFriends() as BasicState.SuccessStateWithResources<*>).data,
            friendsModel
        )
    }

    @Test
    fun testGetFriendsError() = runTest {
        coEvery { friendDataSource.getFriends() } returns BasicState.ErrorState
        Assert.assertTrue(friendRepository.getFriends() is BasicState.ErrorState)
    }

    @Test
    fun testGetIncomingRequestsCountSuccessful() = runTest {
        coEvery { friendDataSource.getIncomingRequestsCount() } returns BasicState.SuccessStateWithResources(
            3
        )
        Assert.assertTrue(friendRepository.getIncomingRequestsCount() is BasicState.SuccessStateWithResources<*>)
        Assert.assertEquals(
            (friendRepository.getIncomingRequestsCount() as BasicState.SuccessStateWithResources<*>).data,
            3
        )
    }

    @Test
    fun testGetIncomingRequestsCountError() = runTest {
        coEvery { friendDataSource.getIncomingRequestsCount() } returns BasicState.ErrorState
        Assert.assertTrue(friendRepository.getIncomingRequestsCount() is BasicState.ErrorState)
    }
}
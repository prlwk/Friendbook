package com.src.book.domain.repository

import com.src.book.LOGIN
import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.data.repository.FriendRepositoryImpl
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

    @MockK
    private lateinit var friendDataSource: FriendDataSource
    private lateinit var friendRepository: FriendRepository

    @Before
    fun setUp() {
        friendRepository = FriendRepositoryImpl(friendDataSource)
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
        coEvery {friendDataSource.sendFriendRequest(any()) } returns SendFriendRequestState.ErrorState
        Assert.assertEquals(
            SendFriendRequestState.ErrorState,
            friendRepository.sendFriendRequest(LOGIN)
        )
    }
}
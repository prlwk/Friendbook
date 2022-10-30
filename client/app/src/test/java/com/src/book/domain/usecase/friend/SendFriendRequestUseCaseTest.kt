package com.src.book.domain.usecase.friend

import com.src.book.LOGIN
import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.utils.SendFriendRequestState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class SendFriendRequestUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendRepository: FriendRepository
    private lateinit var sendFriendRequestUseCase: SendFriendRequestUseCase

    @Before
    fun setUp() {
        sendFriendRequestUseCase = SendFriendRequestUseCase(friendRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testSuccessful() = runTest {
        coEvery { friendRepository.sendFriendRequest(any()) } returns SendFriendRequestState.SuccessState
        Assert.assertEquals(
            SendFriendRequestState.SuccessState, sendFriendRequestUseCase.execute(
                LOGIN
            )
        )
    }

    @Test
    fun testSuchRequestAlreadyExists() = runTest {
        coEvery { friendRepository.sendFriendRequest(any()) } returns SendFriendRequestState.SuchRequestAlreadyExists
        Assert.assertEquals(
            SendFriendRequestState.SuchRequestAlreadyExists, sendFriendRequestUseCase.execute(
                LOGIN
            )
        )
    }

    @Test
    fun testFriendAlreadyExists() = runTest {
        coEvery { friendRepository.sendFriendRequest(any()) } returns SendFriendRequestState.FriendAlreadyExists
        Assert.assertEquals(
            SendFriendRequestState.FriendAlreadyExists, sendFriendRequestUseCase.execute(
                LOGIN
            )
        )
    }

    @Test
    fun testError() = runTest {
        coEvery { friendRepository.sendFriendRequest(any()) } returns SendFriendRequestState.ErrorState
        Assert.assertEquals(
            SendFriendRequestState.ErrorState, sendFriendRequestUseCase.execute(
                LOGIN
            )
        )
    }
}
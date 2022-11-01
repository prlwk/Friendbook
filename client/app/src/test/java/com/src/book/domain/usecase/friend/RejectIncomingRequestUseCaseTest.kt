package com.src.book.domain.usecase.friend

import com.src.book.ID
import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class RejectIncomingRequestUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendRepository: FriendRepository
    private lateinit var rejectIncomingFriendRequestUseCase: RejectIncomingFriendRequestUseCase

    @Before
    fun setUp() {
        rejectIncomingFriendRequestUseCase = RejectIncomingFriendRequestUseCase(friendRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testExecuteSuccessful() = runTest {
        val state = BasicState.SuccessState
        coEvery { friendRepository.rejectIncomingFriendRequest(any()) } returns state
        Assert.assertEquals(state, rejectIncomingFriendRequestUseCase.execute(ID))
    }

    @Test
    fun testExecuteError() = runTest {
        val state =
            BasicState.ErrorState
        coEvery { friendRepository.rejectIncomingFriendRequest(any()) } returns state
        Assert.assertEquals(state, rejectIncomingFriendRequestUseCase.execute(ID))
    }
}
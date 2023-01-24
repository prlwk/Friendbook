package com.src.book.domain.usecase.friend

import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetIncomingRequestsCountUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendRepository: FriendRepository
    private lateinit var getIncomingRequestsCountUseCase: GetIncomingRequestsCountUseCase

    @Before
    fun setUp() {
        getIncomingRequestsCountUseCase =
            GetIncomingRequestsCountUseCase(friendRepository = friendRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testExecuteSuccessful() = runTest {
        coEvery { friendRepository.getIncomingRequestsCount() } returns BasicState.SuccessState(
            3
        )
        Assert.assertTrue(getIncomingRequestsCountUseCase.execute() is BasicState.SuccessState<*>)

        Assert.assertEquals(
            (getIncomingRequestsCountUseCase.execute() as BasicState.SuccessState<*>).data,
            3
        )
    }

    @Test
    fun testExecuteError() = runTest {
        coEvery { friendRepository.getIncomingRequestsCount() } returns BasicState.ErrorState()
        Assert.assertTrue(getIncomingRequestsCountUseCase.execute() is BasicState.ErrorState)
    }
}
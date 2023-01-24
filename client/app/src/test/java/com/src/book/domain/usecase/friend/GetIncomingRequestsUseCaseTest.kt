package com.src.book.domain.usecase.friend

import com.src.book.TestModelsGenerator
import com.src.book.domain.model.friend.FriendRequest.FriendRequest
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
class GetIncomingRequestsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendRepository: FriendRepository
    private lateinit var getIncomingRequestsUseCase: GetIncomingRequestsUseCase
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        getIncomingRequestsUseCase = GetIncomingRequestsUseCase(friendRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testExecuteSuccessful() = runTest {
        val state =
            BasicState.SuccessState(listOf(testModelsGenerator.generateFriendRequestModel()))
        coEvery { friendRepository.getIncomingRequests() } returns state
        Assert.assertEquals(state, getIncomingRequestsUseCase.execute())
    }

    @Test
    fun testExecuteError() = runTest {
        val state = BasicState.ErrorState<List<FriendRequest>>()
        coEvery { friendRepository.getIncomingRequests() } returns state
        Assert.assertEquals(state, getIncomingRequestsUseCase.execute())
    }
}
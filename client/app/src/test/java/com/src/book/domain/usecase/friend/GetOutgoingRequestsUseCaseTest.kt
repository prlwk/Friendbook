package com.src.book.domain.usecase.friend

import com.src.book.TestModelsGenerator
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
class GetOutgoingRequestsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendRepository: FriendRepository
    private lateinit var getOutgoingFriendRequestUseCase: GetOutgoingRequestsUseCase
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        getOutgoingFriendRequestUseCase = GetOutgoingRequestsUseCase(friendRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testExecuteSuccessful() = runTest {
        val state =
            BasicState.SuccessStateWithResources(listOf(testModelsGenerator.generateFriendRequestModel()))
        coEvery { friendRepository.getOutgoingRequests() } returns state
        Assert.assertEquals(state, getOutgoingFriendRequestUseCase.execute())
    }

    @Test
    fun testExecuteError() = runTest {
        val state =
            BasicState.ErrorState
        coEvery { friendRepository.getOutgoingRequests() } returns state
        Assert.assertEquals(state, getOutgoingFriendRequestUseCase.execute())
    }
}
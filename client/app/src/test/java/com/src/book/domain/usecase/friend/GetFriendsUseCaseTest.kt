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
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFriendsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendRepository: FriendRepository
    private lateinit var getFriendsUseCase: GetFriendsUseCase
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        getFriendsUseCase = GetFriendsUseCase(friendRepository = friendRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetFriendsSuccessful() = runTest {
        val friendsModel = listOf(testModelsGenerator.generateFriendModel())
        coEvery { friendRepository.getFriends() } returns BasicState.SuccessState(
            friendsModel
        )
        Assert.assertTrue(getFriendsUseCase.execute() is BasicState.SuccessState<*>)
        Assert.assertEquals(
            (getFriendsUseCase.execute() as BasicState.SuccessState<*>).data,
            friendsModel
        )
    }

    @Test
    fun testGetFriendsError() = runTest {
        coEvery { friendRepository.getFriends() } returns BasicState.ErrorState()
        Assert.assertTrue(getFriendsUseCase.execute() is BasicState.ErrorState)
    }
}
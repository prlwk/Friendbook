package com.src.book.domain.usecase

import com.src.book.PASSWORD
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class ChangePasswordUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var userRepository: UserRepository
    private lateinit var changePasswordUseCase: ChangePasswordUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        changePasswordUseCase = ChangePasswordUseCase(userRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testChangePasswordUseCaseSuccessful() = runTest {
        coEvery { userRepository.changePassword(PASSWORD) } returns BasicState.SuccessState
        Assert.assertEquals(BasicState.SuccessState, changePasswordUseCase.execute(PASSWORD))
    }

    @Test
    fun testChangePasswordUseCaseError() = runTest {
        coEvery { userRepository.changePassword(PASSWORD) } returns BasicState.ErrorState
        Assert.assertEquals(BasicState.ErrorState, changePasswordUseCase.execute(PASSWORD))
    }
}
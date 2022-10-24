package com.src.book.domain.usecase

import com.src.book.PASSWORD
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.ChangePasswordState
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
        coEvery {
            userRepository.changePassword(
                any(),
                any()
            )
        } returns ChangePasswordState.SuccessState
        Assert.assertEquals(
            ChangePasswordState.SuccessState, changePasswordUseCase.execute(
                PASSWORD,
                PASSWORD
            )
        )
    }

    @Test
    fun testChangePasswordUseCaseError() = runTest {
        coEvery {
            userRepository.changePassword(
                any(),
                any()
            )
        } returns ChangePasswordState.ErrorState
        Assert.assertEquals(
            ChangePasswordState.ErrorState, changePasswordUseCase.execute(
                PASSWORD,
                PASSWORD
            )
        )
    }
    @Test
    fun testChangePasswordUseCaseWrong() = runTest {
        coEvery {
            userRepository.changePassword(
                any(),
                any()
            )
        } returns ChangePasswordState.WrongPasswordState
        Assert.assertEquals(
            ChangePasswordState.WrongPasswordState, changePasswordUseCase.execute(
                PASSWORD,
                PASSWORD
            )
        )
    }
}
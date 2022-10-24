package com.src.book.domain.usecase

import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class LogoutUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var userRepository: UserRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setUp() {
        logoutUseCase = LogoutUseCase(userRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testLogoutSuccessful() = runTest {
        coEvery { userRepository.logout() } returns BasicState.SuccessState
        Assert.assertEquals(BasicState.SuccessState, logoutUseCase.execute())
    }

    @Test
    fun testLogoutError() = runTest {
        coEvery { userRepository.logout() } returns BasicState.ErrorState
        Assert.assertEquals(BasicState.ErrorState, logoutUseCase.execute())
    }
}
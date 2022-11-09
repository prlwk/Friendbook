package com.src.book.domain.usecase.login

import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
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
class SendCodeForConfirmationsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var loginRepository: LoginRepository
    private lateinit var sendCodeForConfirmationsUseCase: SendCodeForConfirmationsUseCase

    @Before
    fun setUp() {
        sendCodeForConfirmationsUseCase =
            SendCodeForConfirmationsUseCase(loginRepository = loginRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun executeSuccessful() = runTest {
        coEvery { loginRepository.sendCodeForAccountConfirmations() } returns BasicState.SuccessState
        Assert.assertTrue(sendCodeForConfirmationsUseCase.execute() is BasicState.SuccessState)
    }

    @Test
    fun executeError() = runTest {
        coEvery { loginRepository.sendCodeForAccountConfirmations() } returns BasicState.ErrorState
        Assert.assertTrue(sendCodeForConfirmationsUseCase.execute() is BasicState.ErrorState)
    }
}
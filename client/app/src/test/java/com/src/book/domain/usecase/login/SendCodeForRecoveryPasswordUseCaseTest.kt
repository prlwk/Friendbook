package com.src.book.domain.usecase.login

import com.src.book.EMAIL
import com.src.book.domain.repository.LoginRepository
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
class SendCodeForRecoveryPasswordUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var loginRepository: LoginRepository
    private lateinit var sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase

    @Before
    fun setUp() {
        sendCodeForRecoveryPasswordUseCase = SendCodeForRecoveryPasswordUseCase(loginRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testSendCodeForRecoveryPasswordSuccessful() = runTest {
        coEvery { loginRepository.sendCodeForRecoveryPassword(any()) } returns CodeState.SuccessState
        Assert.assertEquals(
            CodeState.SuccessState,
            sendCodeForRecoveryPasswordUseCase.execute(EMAIL)
        )
    }

    @Test
    fun testSendCodeForRecoveryPasswordWrongEmail() = runTest {
        coEvery { loginRepository.sendCodeForRecoveryPassword(any()) } returns CodeState.WrongEmailState
        Assert.assertEquals(
            CodeState.WrongEmailState,
            sendCodeForRecoveryPasswordUseCase.execute(EMAIL)
        )
    }

    @Test
    fun testSendCodeForRecoveryPasswordError() = runTest {
        coEvery { loginRepository.sendCodeForRecoveryPassword(any()) } returns CodeState.ErrorState
        Assert.assertEquals(
            CodeState.ErrorState,
            sendCodeForRecoveryPasswordUseCase.execute(EMAIL)
        )
    }
}
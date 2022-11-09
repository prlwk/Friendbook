package com.src.book.domain.usecase.login

import com.src.book.CODE
import com.src.book.EMAIL
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.CodeState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class CheckRecoveryCodeForConfirmationsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var loginRepository: LoginRepository
    private lateinit var checkRecoveryCodeForConfirmationsUseCase: CheckRecoveryCodeForConfirmationsUseCase

    @Before
    fun setUp() {
        checkRecoveryCodeForConfirmationsUseCase =
            CheckRecoveryCodeForConfirmationsUseCase(loginRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testExecuteSuccessful() = runTest {
        coEvery {
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                any(),
                any()
            )
        } returns CodeState.SuccessState
        Assert.assertTrue(
            checkRecoveryCodeForConfirmationsUseCase.execute(EMAIL, CODE) is CodeState.SuccessState
        )
    }

    @Test
    fun testExecuteWrongCodeError() = runTest {
        coEvery {
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                any(),
                any()
            )
        } returns CodeState.WrongCodeState
        Assert.assertTrue(
            checkRecoveryCodeForConfirmationsUseCase.execute(
                EMAIL,
                CODE
            ) is CodeState.WrongCodeState
        )
    }

    @Test
    fun testExecuteError() = runTest {
        coEvery {
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                any(),
                any()
            )
        } returns CodeState.ErrorState
        Assert.assertTrue(
            checkRecoveryCodeForConfirmationsUseCase.execute(EMAIL, CODE) is CodeState.ErrorState
        )
    }
}
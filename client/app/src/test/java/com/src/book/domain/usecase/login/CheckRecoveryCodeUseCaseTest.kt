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
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckRecoveryCodeUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var loginRepository: LoginRepository
    private lateinit var checkRecoveryCodeUseCase: CheckRecoveryCodeUseCase

    @Before
    fun setUp() {
        checkRecoveryCodeUseCase = CheckRecoveryCodeUseCase(loginRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testCheckRecoveryCodeSuccessful() = runTest {
        coEvery { loginRepository.checkRecoveryCode(any(), any()) } returns CodeState.SuccessState
        Assert.assertEquals(CodeState.SuccessState, checkRecoveryCodeUseCase.execute(CODE, EMAIL))
    }

    @Test
    fun testCheckRecoveryCodeWrongCode() = runTest {
        coEvery { loginRepository.checkRecoveryCode(any(), any()) } returns CodeState.WrongCodeState
        Assert.assertEquals(CodeState.WrongCodeState, checkRecoveryCodeUseCase.execute(CODE, EMAIL))
    }

    @Test
    fun testCheckRecoveryCodeError() = runTest {
        coEvery { loginRepository.checkRecoveryCode(any(), any()) } returns CodeState.ErrorState
        Assert.assertEquals(CodeState.ErrorState, checkRecoveryCodeUseCase.execute(CODE, EMAIL))
    }
}
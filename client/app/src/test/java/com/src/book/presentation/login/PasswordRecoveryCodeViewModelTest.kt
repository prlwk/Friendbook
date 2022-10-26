package com.src.book.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.CODE
import com.src.book.EMAIL
import com.src.book.domain.usecase.login.CheckRecoveryCodeUseCase
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import com.src.book.domain.utils.CodeState
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryCode.PasswordRecoveryCodeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PasswordRecoveryCodeViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var checkRecoveryCodeUseCase: CheckRecoveryCodeUseCase
    private lateinit var sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase

    private lateinit var passwordRecoveryCodeViewModel: PasswordRecoveryCodeViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        sendCodeForRecoveryPasswordUseCase = mockk()
        checkRecoveryCodeUseCase = mockk()
        passwordRecoveryCodeViewModel = PasswordRecoveryCodeViewModel(
            checkRecoveryCodeUseCase = checkRecoveryCodeUseCase,
            sendCodeForRecoveryPasswordUseCase = sendCodeForRecoveryPasswordUseCase
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckRecoveryCodeSuccessful() = runTest {
        coEvery { checkRecoveryCodeUseCase.execute(any(), any()) } returns CodeState.SuccessState
        passwordRecoveryCodeViewModel.checkRecoveryCode(CODE, EMAIL)
        Assert.assertEquals(
            CodeState.SuccessState,
            passwordRecoveryCodeViewModel.liveDataCodeState.value
        )
    }

    @Test
    fun testCheckRecoveryCodeWrongCode() = runTest {
        coEvery { checkRecoveryCodeUseCase.execute(any(), any()) } returns CodeState.WrongCodeState
        passwordRecoveryCodeViewModel.checkRecoveryCode(CODE, EMAIL)
        Assert.assertEquals(
            CodeState.WrongCodeState,
            passwordRecoveryCodeViewModel.liveDataCodeState.value
        )
    }

    @Test
    fun testCheckRecoveryCodeError() = runTest {
        coEvery { checkRecoveryCodeUseCase.execute(any(), any()) } returns CodeState.ErrorState
        passwordRecoveryCodeViewModel.checkRecoveryCode(CODE, EMAIL)
        Assert.assertEquals(
            CodeState.ErrorState,
            passwordRecoveryCodeViewModel.liveDataCodeState.value
        )
    }

    @Test
    fun testSendCodeForRecoveryPasswordSuccessful() = runTest {
        coEvery { sendCodeForRecoveryPasswordUseCase.execute(any())} returns CodeState.SuccessState
        passwordRecoveryCodeViewModel.sendRepeatingCodeState(EMAIL)
        Assert.assertEquals(
            CodeState.SuccessState,
            passwordRecoveryCodeViewModel.liveDataRepeatingCodeState.value
        )
    }

    @Test
    fun testSendCodeForRecoveryPasswordWrongEmail() = runTest {
        coEvery { sendCodeForRecoveryPasswordUseCase.execute(any())} returns CodeState.WrongEmailState
        passwordRecoveryCodeViewModel.sendRepeatingCodeState(EMAIL)
        Assert.assertEquals(
            CodeState.WrongEmailState,
            passwordRecoveryCodeViewModel.liveDataRepeatingCodeState.value
        )
    }
    @Test
    fun testSendCodeForRecoveryPasswordError() = runTest {
        coEvery { sendCodeForRecoveryPasswordUseCase.execute(any())} returns CodeState.ErrorState
        passwordRecoveryCodeViewModel.sendRepeatingCodeState(EMAIL)
        Assert.assertEquals(
            CodeState.ErrorState,
            passwordRecoveryCodeViewModel.liveDataRepeatingCodeState.value
        )
    }
}
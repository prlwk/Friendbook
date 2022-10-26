package com.src.book.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.EMAIL
import com.src.book.domain.usecase.login.SendCodeForRecoveryPasswordUseCase
import com.src.book.domain.utils.CodeState
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryEmail.PasswordRecoveryEmailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class PasswordRecoveryEmailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var sendCodeForRecoveryPasswordUseCase: SendCodeForRecoveryPasswordUseCase

    private lateinit var passwordRecoveryEmailViewModel: PasswordRecoveryEmailViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        sendCodeForRecoveryPasswordUseCase = mockk()
        passwordRecoveryEmailViewModel =
            PasswordRecoveryEmailViewModel(sendCodeForRecoveryPasswordUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckEmailExistsTrueSuccessful() = runTest {
        coEvery { sendCodeForRecoveryPasswordUseCase.execute(any()) } returns CodeState.SuccessState
        passwordRecoveryEmailViewModel.sendCode(EMAIL)
        Assert.assertEquals(
            CodeState.SuccessState, passwordRecoveryEmailViewModel.liveDataCodeState.value
        )
    }

    @Test
    fun testCheckEmailExistsFalseSuccessful() = runTest {
        coEvery { sendCodeForRecoveryPasswordUseCase.execute(any()) } returns CodeState.ErrorState
        passwordRecoveryEmailViewModel.sendCode(EMAIL)
        Assert.assertEquals(
            CodeState.ErrorState, passwordRecoveryEmailViewModel.liveDataCodeState.value
        )
    }
}
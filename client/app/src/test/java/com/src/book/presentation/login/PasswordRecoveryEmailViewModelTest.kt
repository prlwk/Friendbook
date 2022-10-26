package com.src.book.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.EMAIL
import com.src.book.domain.usecase.login.CheckEmailExistsUseCase
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
    private lateinit var checkEmailExistsUseCase: CheckEmailExistsUseCase

    private lateinit var passwordRecoveryEmailViewModel: PasswordRecoveryEmailViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        checkEmailExistsUseCase = mockk()
        passwordRecoveryEmailViewModel = PasswordRecoveryEmailViewModel(checkEmailExistsUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckEmailExistsTrueSuccessful() = runTest {
        coEvery { checkEmailExistsUseCase.execute(any()) } returns true
        passwordRecoveryEmailViewModel.checkEmailExists(EMAIL)
        Assert.assertEquals(
            true, passwordRecoveryEmailViewModel.liveDataEmailExists.value
        )
        Assert.assertEquals(
            false, passwordRecoveryEmailViewModel.liveDataIsLoading.value
        )
    }

    @Test
    fun testCheckEmailExistsFalseSuccessful() = runTest {
        coEvery { checkEmailExistsUseCase.execute(any()) } returns false
        passwordRecoveryEmailViewModel.checkEmailExists(EMAIL)
        Assert.assertEquals(
            false, passwordRecoveryEmailViewModel.liveDataEmailExists.value
        )
        Assert.assertEquals(
            false, passwordRecoveryEmailViewModel.liveDataIsLoading.value
        )
    }
}
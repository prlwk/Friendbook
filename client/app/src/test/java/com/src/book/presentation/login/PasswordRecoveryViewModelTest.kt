package com.src.book.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.PASSWORD
import com.src.book.domain.usecase.user.ChangePasswordUseCase
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecovery.PasswordRecoveryViewModel
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
class PasswordRecoveryViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var changePasswordUseCase: ChangePasswordUseCase

    private lateinit var passwordRecoveryViewModel: PasswordRecoveryViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        changePasswordUseCase = mockk()
        passwordRecoveryViewModel =
            PasswordRecoveryViewModel(changePasswordUseCase = changePasswordUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testChangePasswordSuccessful() = runTest {
        coEvery {
            changePasswordUseCase.execute(
                any(),
                any()
            )
        } returns ChangePasswordState.SuccessState
        passwordRecoveryViewModel.changePassword(PASSWORD)
        Assert.assertEquals(
            ChangePasswordState.SuccessState,
            passwordRecoveryViewModel.liveDataChangePasswordState.value
        )
    }

    @Test
    fun testChangePasswordError() = runTest {
        coEvery {
            changePasswordUseCase.execute(
                any(),
                any()
            )
        } returns ChangePasswordState.ErrorState
        passwordRecoveryViewModel.changePassword(PASSWORD)
        Assert.assertEquals(
            ChangePasswordState.ErrorState,
            passwordRecoveryViewModel.liveDataChangePasswordState.value
        )
    }
}
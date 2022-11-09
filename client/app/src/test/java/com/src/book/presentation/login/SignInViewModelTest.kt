package com.src.book.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.EMAIL
import com.src.book.PASSWORD
import com.src.book.domain.usecase.login.LoginAsGuestUseCase
import com.src.book.domain.usecase.login.SignInUseCase
import com.src.book.domain.utils.LoginState
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModel
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
class SignInViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var signInViewModel: SignInViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var loginAsGuestUseCase: LoginAsGuestUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        signInUseCase = mockk()
        loginAsGuestUseCase = mockk()
        signInViewModel = SignInViewModel(
            signInUseCase = signInUseCase,
            loginAsGuestUseCase = loginAsGuestUseCase
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun signInRequestSuccessful() = runTest {
        coEvery { signInUseCase.execute(any(), any(), any()) } returns LoginState.SuccessState
        signInViewModel.signInRequest(EMAIL, PASSWORD, true)
        Assert.assertEquals(LoginState.SuccessState, signInViewModel.liveDataLoginState.value)
        Assert.assertEquals(false, signInViewModel.liveDataIsLoading.value)
    }

    @Test
    fun signInRequestError() = runTest {
        coEvery { signInUseCase.execute(any(), any(), any()) } returns LoginState.ErrorServerState
        signInViewModel.signInRequest(EMAIL, PASSWORD, true)
        Assert.assertEquals(LoginState.ErrorServerState, signInViewModel.liveDataLoginState.value)
        Assert.assertEquals(false, signInViewModel.liveDataIsLoading.value)
    }

    @Test
    fun signInRequestErrorEmailLogin() = runTest {
        coEvery {
            signInUseCase.execute(any(), any(), any())
        } returns LoginState.ErrorEmailLoginState
        signInViewModel.signInRequest(EMAIL, PASSWORD, true)
        Assert.assertEquals(
            LoginState.ErrorEmailLoginState,
            signInViewModel.liveDataLoginState.value
        )
        Assert.assertEquals(false, signInViewModel.liveDataIsLoading.value)
    }

    @Test
    fun signInRequestErrorPassword() = runTest {
        coEvery { signInUseCase.execute(any(), any(), any()) } returns LoginState.ErrorPasswordState
        signInViewModel.signInRequest(EMAIL, PASSWORD, true)
        Assert.assertEquals(LoginState.ErrorPasswordState, signInViewModel.liveDataLoginState.value)
        Assert.assertEquals(false, signInViewModel.liveDataIsLoading.value)
    }
}
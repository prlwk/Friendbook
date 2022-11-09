package com.src.book.presentation.login

import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.*
import com.src.book.domain.usecase.login.*
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.RegistrationState
import io.mockk.mockk

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var checkEmailExistsUseCase: CheckEmailExistsUseCase
    private lateinit var checkRecoveryCodeForConfirmationsUseCase: CheckRecoveryCodeForConfirmationsUseCase
    private lateinit var loginAsGuestUseCase: LoginAsGuestUseCase
    private lateinit var registrationUseCase: RegistrationUseCase
    private lateinit var sendCodeForConfirmationsUseCase: SendCodeForConfirmationsUseCase
    private lateinit var registrationViewModel: RegistrationViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        checkEmailExistsUseCase = mockk()
        checkRecoveryCodeForConfirmationsUseCase = mockk()
        loginAsGuestUseCase = mockk()
        registrationUseCase = mockk()
        sendCodeForConfirmationsUseCase = mockk()

        registrationViewModel =
            RegistrationViewModel(
                checkEmailExistsUseCase = checkEmailExistsUseCase,
                checkRecoveryCodeForConfirmationsUseCase = checkRecoveryCodeForConfirmationsUseCase,
                loginAsGuestUseCase = loginAsGuestUseCase,
                registrationUseCase = registrationUseCase,
                sendCodeForConfirmationsUseCase = sendCodeForConfirmationsUseCase
            )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckEmailExistsSuccessful() = runTest {
        coEvery { checkEmailExistsUseCase.execute(any()) } returns BasicState.SuccessStateWithResources(
            true
        )
        registrationViewModel.checkEmailExists(EMAIL)
        Assert.assertTrue(registrationViewModel.liveDataEmailExists.value is BasicState.SuccessStateWithResources<*>)
        Assert.assertEquals(
            (registrationViewModel.liveDataEmailExists.value as BasicState.SuccessStateWithResources<*>).data,
            true
        )
    }

    @Test
    fun testCheckEmailExistsError() = runTest {
        coEvery { checkEmailExistsUseCase.execute(any()) } returns BasicState.ErrorState
        registrationViewModel.checkEmailExists(EMAIL)
        Assert.assertTrue(registrationViewModel.liveDataEmailExists.value is BasicState.ErrorState)
    }

    @Test
    fun testRegistrationSuccessful() = runTest {
        coEvery {
            registrationUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns RegistrationState.SuccessState
        registrationViewModel.setEmail(EMAIL)
        registrationViewModel.setLogin(LOGIN)
        registrationViewModel.setName(NAME)
        registrationViewModel.setPassword(PASSWORD)
        Assert.assertNotNull(registrationViewModel.liveDataEmail.value)
        Assert.assertNotNull(registrationViewModel.liveDataLogin.value)
        Assert.assertNotNull(registrationViewModel.liveDataName.value)
        Assert.assertNotNull(registrationViewModel.liveDataPassword.value)
        registrationViewModel.registration(null)
        Assert.assertTrue(registrationViewModel.liveDataRegistration.value is RegistrationState.SuccessState)
    }

    @Test
    fun testRegistrationLoginAlreadyExistsError() = runTest {
        coEvery {
            registrationUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns RegistrationState.LoginAlreadyExistsState
        registrationViewModel.setEmail(EMAIL)
        registrationViewModel.setLogin(LOGIN)
        registrationViewModel.setName(NAME)
        registrationViewModel.setPassword(PASSWORD)
        registrationViewModel.registration(null)
        Assert.assertNotNull(registrationViewModel.liveDataEmail.value)
        Assert.assertNotNull(registrationViewModel.liveDataLogin.value)
        Assert.assertNotNull(registrationViewModel.liveDataName.value)
        Assert.assertNotNull(registrationViewModel.liveDataPassword.value)
        Assert.assertTrue(registrationViewModel.liveDataRegistration.value is RegistrationState.LoginAlreadyExistsState)
    }

    @Test
    fun testRegistrationError() = runTest {
        coEvery {
            registrationUseCase.execute(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns RegistrationState.ErrorState
        registrationViewModel.registration(null)
        Assert.assertTrue(registrationViewModel.liveDataRegistration.value is RegistrationState.ErrorState)
    }

    @Test
    fun testCheckRecoveryCodeSuccessful() = runTest {
        coEvery {
            checkRecoveryCodeForConfirmationsUseCase.execute(
                any(),
                any()
            )
        } returns CodeState.SuccessState
        registrationViewModel.setEmail(EMAIL)
        registrationViewModel.checkRecoveryCode(CODE)
        Assert.assertTrue(registrationViewModel.liveDataCodeState.value is CodeState.SuccessState)
    }

    @Test
    fun testCheckRecoveryCodeWrongCodeError() = runTest {
        coEvery {
            checkRecoveryCodeForConfirmationsUseCase.execute(
                any(),
                any()
            )
        } returns CodeState.WrongCodeState
        registrationViewModel.setEmail(EMAIL)
        registrationViewModel.checkRecoveryCode(CODE)
        Assert.assertTrue(registrationViewModel.liveDataCodeState.value is CodeState.WrongCodeState)
    }

    @Test
    fun testCheckRecoveryCodeError() = runTest {
        coEvery {
            checkRecoveryCodeForConfirmationsUseCase.execute(
                any(),
                any()
            )
        } returns CodeState.ErrorState
        registrationViewModel.setEmail(EMAIL)
        registrationViewModel.checkRecoveryCode(CODE)
        Assert.assertTrue(registrationViewModel.liveDataCodeState.value is CodeState.ErrorState)
    }

    @Test
    fun testSendRepeatingCodeSuccessful() = runTest {
        coEvery { sendCodeForConfirmationsUseCase.execute() } returns BasicState.SuccessState
        registrationViewModel.sendRepeatingCode()
        Assert.assertTrue(registrationViewModel.liveDataRepeatingCodeState.value is BasicState.SuccessState)
    }

    @Test
    fun testSendRepeatingCodeError() = runTest {
        coEvery { sendCodeForConfirmationsUseCase.execute() } returns BasicState.ErrorState
        registrationViewModel.sendRepeatingCode()
        Assert.assertTrue(registrationViewModel.liveDataRepeatingCodeState.value is BasicState.ErrorState)
    }
}
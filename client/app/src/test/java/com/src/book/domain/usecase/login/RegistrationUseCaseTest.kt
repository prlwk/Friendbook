package com.src.book.domain.usecase.login

import com.src.book.EMAIL
import com.src.book.LOGIN
import com.src.book.NAME
import com.src.book.PASSWORD
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.RegistrationState
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
class RegistrationUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var loginRepository: LoginRepository
    private lateinit var registrationUseCase: RegistrationUseCase

    @Before
    fun setUp() {
        registrationUseCase = RegistrationUseCase(loginRepository = loginRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testRegistrationSuccessful() = runTest {
        coEvery {
            loginRepository.registration(
                any(),
                any()
            )
        } returns RegistrationState.SuccessState
        Assert.assertTrue(
            registrationUseCase.execute(
                email = EMAIL,
                password = PASSWORD,
                login = LOGIN,
                name = NAME,
                uri = null
            ) is RegistrationState.SuccessState
        )
    }

    @Test
    fun testRegistrationLoginAlreadyExistsError() = runTest {
        coEvery {
            loginRepository.registration(
                any(),
                any()
            )
        } returns RegistrationState.LoginAlreadyExistsState
        Assert.assertTrue(
            registrationUseCase.execute(
                email = EMAIL,
                password = PASSWORD,
                login = LOGIN,
                name = NAME,
                uri = null
            ) is RegistrationState.LoginAlreadyExistsState
        )
    }

    @Test
    fun testRegistrationEmailAlreadyExistsError() = runTest {
        coEvery {
            loginRepository.registration(
                any(),
                any()
            )
        } returns RegistrationState.EmailAlreadyExistsState
        Assert.assertTrue(
            registrationUseCase.execute(
                email = EMAIL,
                password = PASSWORD,
                login = LOGIN,
                name = NAME,
                uri = null
            ) is RegistrationState.EmailAlreadyExistsState
        )
    }

    @Test
    fun testRegistrationError() = runTest {
        coEvery {
            loginRepository.registration(
                any(),
                any()
            )
        } returns RegistrationState.ErrorState
        Assert.assertTrue(
            registrationUseCase.execute(
                email = EMAIL,
                password = PASSWORD,
                login = LOGIN,
                name = NAME,
                uri = null
            ) is RegistrationState.ErrorState
        )
    }
}
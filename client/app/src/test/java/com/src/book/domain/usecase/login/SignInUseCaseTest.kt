package com.src.book.domain.usecase.login

import com.src.book.EMAIL
import com.src.book.IS_ENTRY_BY_EMAIL
import com.src.book.PASSWORD
import com.src.book.TestModelsGenerator
import com.src.book.domain.repository.LoginRepository
import com.src.book.domain.utils.LoginState
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class SignInUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var loginRepository: LoginRepository
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setUp() {
        loginRepository = mockk()
        signInUseCase = SignInUseCase(loginRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testSignInSuccessful() = runTest {
        coEvery { loginRepository.signIn(any()) } returns LoginState.SuccessState
        Assert.assertEquals(
            LoginState.SuccessState,
            signInUseCase.execute(EMAIL, PASSWORD, IS_ENTRY_BY_EMAIL)
        )
    }

    @Test
    fun testSignInErrorPassword() = runTest {
        coEvery { loginRepository.signIn(any()) } returns LoginState.ErrorPasswordState
        Assert.assertEquals(
            LoginState.ErrorPasswordState,
            signInUseCase.execute(EMAIL, "", IS_ENTRY_BY_EMAIL)
        )
    }

    @Test
    fun testSignInErrorServer() = runTest {
        coEvery { loginRepository.signIn(any()) } returns LoginState.ErrorServerState
        Assert.assertEquals(
            LoginState.ErrorServerState,
            signInUseCase.execute(EMAIL, PASSWORD, IS_ENTRY_BY_EMAIL)
        )
    }

    @Test
    fun testSignInErrorEmailLogin() = runTest {
        coEvery { loginRepository.signIn(any()) } returns LoginState.ErrorEmailLoginState
        Assert.assertEquals(
            LoginState.ErrorEmailLoginState,
            signInUseCase.execute(EMAIL, PASSWORD, IS_ENTRY_BY_EMAIL)
        )
    }
}
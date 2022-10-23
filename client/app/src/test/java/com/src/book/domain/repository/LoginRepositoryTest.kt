package com.src.book.domain.repository

import com.src.book.EMAIL
import com.src.book.TestModelsGenerator
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.repository.LoginRepositoryImpl
import com.src.book.domain.utils.LoginState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class LoginRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var loginDataSource: LoginDataSource
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setUp() {
        loginRepository = LoginRepositoryImpl(loginDataSource)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testSignInSuccessful() = runTest {
        val login = testModelsGenerator.generateLoginModel()
        coEvery { loginDataSource.signIn(any()) } returns LoginState.SuccessState
        Assert.assertEquals(
            LoginState.SuccessState,
            loginRepository.signIn(login)
        )
    }

    @Test
    fun testSignInErrorServer() = runTest {
        val login = testModelsGenerator.generateLoginModel()
        coEvery { loginDataSource.signIn(any()) } returns LoginState.ErrorServerState
        Assert.assertEquals(
            LoginState.ErrorServerState,
            loginRepository.signIn(login)
        )
    }

    @Test
    fun testSignInErrorPassword() = runTest {
        val login = testModelsGenerator.generateLoginModel()
        coEvery { loginDataSource.signIn(any()) } returns LoginState.ErrorPasswordState
        Assert.assertEquals(
            LoginState.ErrorPasswordState,
            loginRepository.signIn(login)
        )
    }

    @Test
    fun testSignInErrorEmailLogin() = runTest {
        val login = testModelsGenerator.generateLoginModel()
        coEvery { loginDataSource.signIn(any()) } returns LoginState.ErrorEmailLoginState
        Assert.assertEquals(
            LoginState.ErrorEmailLoginState,
            loginRepository.signIn(login)
        )
    }

    @Test
    fun testCheckEmailExistsSuccessful() = runTest {
        coEvery { loginDataSource.checkEmailExists(any()) } returns false
        Assert.assertEquals(
            false,
            loginRepository.checkEmailExists(EMAIL)
        )
    }
}
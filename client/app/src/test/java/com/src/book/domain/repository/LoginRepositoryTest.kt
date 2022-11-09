package com.src.book.domain.repository

import com.src.book.CODE
import com.src.book.EMAIL
import com.src.book.TestModelsGenerator
import com.src.book.data.local.LocalUserRepository
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.repository.LoginRepositoryImpl
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.LoginState
import com.src.book.domain.utils.RegistrationState
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

    @MockK
    private lateinit var userLocalUserRepository: LocalUserRepository
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setUp() {
        loginRepository =
            LoginRepositoryImpl(loginDataSource, userLocalRepository = userLocalUserRepository)
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
        coEvery { loginDataSource.checkEmailExists(any()) } returns BasicState.SuccessStateWithResources(
            true
        )
        Assert.assertTrue(
            loginRepository.checkEmailExists(EMAIL) is BasicState.SuccessStateWithResources<*>
        )
        Assert.assertEquals(
            (loginRepository.checkEmailExists(EMAIL) as BasicState.SuccessStateWithResources<*>).data,
            true
        )
    }

    @Test
    fun testCheckEmailExistsError() = runTest {
        coEvery { loginDataSource.checkEmailExists(any()) } returns BasicState.ErrorState
        Assert.assertTrue(loginRepository.checkEmailExists(EMAIL) is BasicState.ErrorState)
    }

    @Test
    fun testCheckRecoveryCodeSuccessful() = runTest {
        coEvery { loginDataSource.checkRecoveryCode(any(), any()) } returns CodeState.SuccessState
        Assert.assertTrue(loginRepository.checkRecoveryCode(CODE, EMAIL) is CodeState.SuccessState)
    }

    @Test
    fun testCheckRecoveryCodeWrongCodeError() = runTest {
        coEvery { loginDataSource.checkRecoveryCode(any(), any()) } returns CodeState.WrongCodeState
        Assert.assertTrue(
            loginRepository.checkRecoveryCode(
                CODE,
                EMAIL
            ) is CodeState.WrongCodeState
        )
    }

    @Test
    fun testCheckRecoveryCodeError() = runTest {
        coEvery { loginDataSource.checkRecoveryCode(any(), any()) } returns CodeState.ErrorState
        Assert.assertTrue(
            loginRepository.checkRecoveryCode(
                CODE,
                EMAIL
            ) is CodeState.ErrorState
        )
    }

    @Test
    fun testSendCodeForRecoveryPasswordSuccessful() = runTest {
        coEvery { loginDataSource.sendCodeForRecoveryPassword(any()) } returns CodeState.SuccessState
        Assert.assertTrue(loginRepository.sendCodeForRecoveryPassword(EMAIL) is CodeState.SuccessState)
    }

    @Test
    fun testSendCodeForRecoveryPasswordError() = runTest {
        coEvery { loginDataSource.sendCodeForRecoveryPassword(any()) } returns CodeState.ErrorState
        Assert.assertTrue(loginRepository.sendCodeForRecoveryPassword(EMAIL) is CodeState.ErrorState)
    }

    @Test
    fun testRegistrationSuccessful() = runTest {
        coEvery {
            loginDataSource.registration(
                any(),
                any()
            )
        } returns RegistrationState.SuccessState
        Assert.assertTrue(
            loginRepository.registration(
                "data",
                null
            ) is RegistrationState.SuccessState
        )
    }

    @Test
    fun testRegistrationEmailAlreadyExistsError() = runTest {
        coEvery {
            loginDataSource.registration(
                any(),
                any()
            )
        } returns RegistrationState.EmailAlreadyExistsState
        Assert.assertTrue(
            loginRepository.registration(
                "data",
                null
            ) is RegistrationState.EmailAlreadyExistsState
        )
    }

    @Test
    fun testRegistrationLoginAlreadyExistsError() = runTest {
        coEvery {
            loginDataSource.registration(
                any(),
                any()
            )
        } returns RegistrationState.LoginAlreadyExistsState
        Assert.assertTrue(
            loginRepository.registration(
                "data",
                null
            ) is RegistrationState.LoginAlreadyExistsState
        )
    }

    @Test
    fun testCheckRecoveryCodeForAccountConfirmationsSuccessful() = runTest {
        coEvery {
            loginDataSource.checkRecoveryCodeForAccountConfirmations(
                any(),
                any()
            )
        } returns CodeState.SuccessState
        Assert.assertTrue(
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                CODE,
                EMAIL
            ) is CodeState.SuccessState
        )
    }

    @Test
    fun testCheckRecoveryCodeForAccountConfirmationsWrongCodeError() = runTest {
        coEvery {
            loginDataSource.checkRecoveryCodeForAccountConfirmations(
                any(),
                any()
            )
        } returns CodeState.WrongCodeState
        Assert.assertTrue(
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                CODE,
                EMAIL
            ) is CodeState.WrongCodeState
        )
    }

    @Test
    fun testCheckRecoveryCodeForAccountConfirmationsError() = runTest {
        coEvery {
            loginDataSource.checkRecoveryCodeForAccountConfirmations(
                any(),
                any()
            )
        } returns CodeState.ErrorState
        Assert.assertTrue(
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                CODE,
                EMAIL
            ) is CodeState.ErrorState
        )
    }

    @Test
    fun testSendCodeForAccountConfirmationsSuccessful() = runTest {
        coEvery { loginDataSource.sendCodeForAccountConfirmations() } returns BasicState.SuccessState
        Assert.assertTrue(loginRepository.sendCodeForAccountConfirmations() is BasicState.SuccessState)
    }

    @Test
    fun testSendCodeForAccountConfirmationsError() = runTest {
        coEvery { loginDataSource.sendCodeForAccountConfirmations() } returns BasicState.ErrorState
        Assert.assertTrue(loginRepository.sendCodeForAccountConfirmations() is BasicState.ErrorState)
    }
}
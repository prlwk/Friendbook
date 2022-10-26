package com.src.book.data.remote.dataSource

import com.src.book.*
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.remote.dataSource.login.LoginDataSourceImpl
import com.src.book.data.remote.model.login.login.LoginMapper
import com.src.book.data.remote.service.LoginService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.utils.INVALID_CODE
import com.src.book.domain.utils.CodeState
import com.src.book.domain.utils.LoginState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.junit.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginDaraSourceTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var loginService: LoginService

    @MockK
    private lateinit var loginMapper: LoginMapper

    @MockK
    private lateinit var sessionStorage: SessionStorage
    private lateinit var loginDataSource: LoginDataSource

    @Before
    fun setUp() {
        loginDataSource = LoginDataSourceImpl(loginService, sessionStorage, loginMapper)
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testSignInSuccessful() = runTest {
        val loginAnswerResponseModel = testModelsResponseGenerator.generateLoginAnswerResponse()
        val loginResponse = testModelsResponseGenerator.generateLoginResponseModel()
        val loginModel = testModelsGenerator.generateLoginModel()
        coEvery { loginService.signIn(any()) } returns Response.success(loginAnswerResponseModel)
        coEvery { loginMapper.mapFromModelToResponse(any()) } returns loginResponse
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(
            LoginState.SuccessState,
            loginDataSource.signIn(loginModel)
        )
    }

    @Test
    fun testSignInErrorEmail() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 404,
                ERROR to "Not Found",
                MESSAGE to ERROR_EMAIL
            )
        )
        val loginResponse = testModelsResponseGenerator.generateLoginResponseModel()
        val loginModel = testModelsGenerator.generateLoginModel()
        coEvery { loginService.signIn(any()) } returns Response.error(
            404, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { loginMapper.mapFromModelToResponse(any()) } returns loginResponse
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(
            LoginState.ErrorEmailLoginState,
            loginDataSource.signIn(loginModel)
        )
    }

    @Test
    fun testSignInErrorNickName() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 404,
                ERROR to "Not Found",
                MESSAGE to ERROR_NICKNAME
            )
        )
        val loginResponse = testModelsResponseGenerator.generateLoginResponseModel()
        val loginModel = testModelsGenerator.generateLoginModel()
        coEvery { loginService.signIn(any()) } returns Response.error(
            404, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { loginMapper.mapFromModelToResponse(any()) } returns loginResponse
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(
            LoginState.ErrorEmailLoginState,
            loginDataSource.signIn(loginModel)
        )
    }

    @Test
    fun testSignInErrorServer() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 500,
                ERROR to "Internal Server Error",
                MESSAGE to MESSAGE
            )
        )
        val loginResponse = testModelsResponseGenerator.generateLoginResponseModel()
        val loginModel = testModelsGenerator.generateLoginModel()
        coEvery { loginService.signIn(any()) } returns Response.error(
            500, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { loginMapper.mapFromModelToResponse(any()) } returns loginResponse
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(
            LoginState.ErrorServerState,
            loginDataSource.signIn(loginModel)
        )
    }

    @Test
    fun testCheckEmailExistsTrueSuccessful() = runTest {
        coEvery { loginService.checkEmailExists(any()) } returns Response.success(
            testModelsResponseGenerator.generateEmailExistsTrueResponse()
        )
        Assert.assertEquals(
            true,
            loginDataSource.checkEmailExists(EMAIL)
        )
    }

    @Test
    fun testCheckEmailExistsFalseSuccessful() = runTest {
        coEvery { loginService.checkEmailExists(any()) } returns Response.success(
            testModelsResponseGenerator.generateEmailExistsFalseResponse()
        )
        Assert.assertEquals(
            false,
            loginDataSource.checkEmailExists(EMAIL)
        )
    }

    @Test
    fun testCheckRecoveryCodeSuccessful() = runTest {
        val loginResponse = testModelsResponseGenerator.generateLoginAnswerResponse()
        coEvery { loginService.checkRecoveryCode(any(), any()) } returns Response.success(
            loginResponse
        )
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(CodeState.SuccessState, loginDataSource.checkRecoveryCode(CODE, EMAIL))
    }

    @Test
    fun testCheckRecoveryCodeWrongCodeState() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 409,
                ERROR to INVALID_CODE,
                MESSAGE to INVALID_CODE
            )
        )
        coEvery { loginService.checkRecoveryCode(any(), any()) } returns Response.error(
            409, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(
            CodeState.WrongCodeState,
            loginDataSource.checkRecoveryCode(CODE, EMAIL)
        )
    }

    @Test
    fun testCheckRecoveryCodeError() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 500,
                ERROR to "error",
                MESSAGE to "message"
            )
        )
        coEvery { loginService.checkRecoveryCode(any(), any()) } returns Response.error(
            404, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.refresh(any(), any(), any(), any(), any(), any()) } returns Unit
        Assert.assertEquals(CodeState.ErrorState, loginDataSource.checkRecoveryCode(CODE, EMAIL))
    }

    @Test
    fun testCodeForRecoveryPasswordSuccessful() = runTest {
        coEvery { loginService.sendCodeForRecoveryPassword(any()) } returns Response.success(Unit)
        Assert.assertEquals(
            CodeState.SuccessState, loginDataSource.sendCodeForRecoveryPassword(EMAIL)
        )
    }

    @Test
    fun testCodeForRecoveryPasswordWrongEmail() = runTest {
        coEvery { loginService.sendCodeForRecoveryPassword(any()) } returns Response.error(
            404,
            "error".toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            CodeState.WrongEmailState, loginDataSource.sendCodeForRecoveryPassword(EMAIL)
        )
    }

    @Test
    fun testCodeForRecoveryPasswordError() = runTest {
        coEvery { loginService.sendCodeForRecoveryPassword(any()) } returns Response.error(
            500,
            "error".toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            CodeState.ErrorState, loginDataSource.sendCodeForRecoveryPassword(EMAIL)
        )
    }

}
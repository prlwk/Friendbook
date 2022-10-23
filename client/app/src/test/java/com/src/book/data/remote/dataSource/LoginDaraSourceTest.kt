package com.src.book.data.remote.dataSource

import com.src.book.*
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.remote.dataSource.login.LoginDataSourceImpl
import com.src.book.data.remote.model.login.login.LoginMapper
import com.src.book.data.remote.service.LoginService
import com.src.book.data.remote.session.SessionStorage
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
}
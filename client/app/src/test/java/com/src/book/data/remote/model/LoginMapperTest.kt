package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.user.login.LoginMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class LoginMapperTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var loginMapper: LoginMapper

    @Before
    fun setUp() {
        loginMapper = LoginMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapLoginResponseSuccessful() = runTest {
        val loginResponseModel = testModelsResponseGenerator.generateLoginResponseModel()
        val loginModel = testModelsGenerator.generateLoginModel()
        val loginResponseResult = loginMapper.mapFromModelToResponse(loginModel)
        Assert.assertEquals(loginResponseModel.loginOrEmail, loginResponseResult.loginOrEmail)
        Assert.assertEquals(loginResponseModel.isEntryByEmail, loginResponseResult.isEntryByEmail)
        Assert.assertEquals(loginResponseModel.password, loginResponseResult.password)
    }
}
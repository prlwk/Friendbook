package com.src.book.data.remote.dataSource

import com.src.book.*
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.remote.dataSource.user.UserDataSourceImpl
import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.service.UserServiceWithToken
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserDataSourceTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var userServiceWithToken: UserServiceWithToken

    @MockK
    private lateinit var sessionStorage: SessionStorage
    private lateinit var userDataSource: UserDataSource

    @Before
    fun setUp() {
        userDataSource = UserDataSourceImpl(userService, userServiceWithToken, sessionStorage)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testChangePasswordSuccessful() = runTest {
        coEvery { userServiceWithToken.changePassword(any()) } returns Response.success(Unit)
        coEvery { sessionStorage.getEmail() } returns EMAIL
        Assert.assertEquals(
            BasicState.SuccessState,
            userDataSource.changePassword(PASSWORD)
        )
    }

    @Test
    fun testChangePasswordError() = runTest {
        coEvery { userServiceWithToken.changePassword(any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.getEmail() } returns EMAIL
        Assert.assertEquals(
            BasicState.ErrorState,
            userDataSource.changePassword(PASSWORD)
        )
    }
}
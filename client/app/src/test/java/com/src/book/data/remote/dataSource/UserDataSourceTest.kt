package com.src.book.data.remote.dataSource

import com.src.book.*
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.remote.dataSource.user.UserDataSourceImpl
import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.utils.ALREADY_FRIENDS
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.SendFriendRequestState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
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
    private lateinit var sessionStorage: SessionStorage
    private lateinit var userDataSource: UserDataSource

    @Before
    fun setUp() {
        userDataSource = UserDataSourceImpl(userService, sessionStorage)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testChangePasswordSuccessful() = runTest {
        coEvery {
            userService.changePassword(
                any(),
                any(),
                any()
            )
        } returns Response.success(Unit)
        coEvery { sessionStorage.getRefreshToken() } returns REFRESH_TOKEN
        Assert.assertEquals(
            ChangePasswordState.SuccessState,
            userDataSource.changePassword(PASSWORD, PASSWORD)
        )
    }

    @Test
    fun testChangePasswordError() = runTest {
        coEvery { userService.changePassword(any(), any(), any()) } returns Response.error(
            500, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.getRefreshToken() } returns REFRESH_TOKEN
        Assert.assertEquals(
            ChangePasswordState.ErrorState,
            userDataSource.changePassword(PASSWORD, PASSWORD)
        )
    }

    @Test
    fun testChangePasswordWrongPassword() = runTest {
        coEvery { userService.changePassword(any(), any(), any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.getRefreshToken() } returns REFRESH_TOKEN
        Assert.assertEquals(
            ChangePasswordState.WrongPasswordState,
            userDataSource.changePassword(PASSWORD, PASSWORD)
        )
    }

    @Test
    fun testLogoutSuccessful() = runTest {
        coEvery { sessionStorage.getRefreshToken() } returns REFRESH_TOKEN
        coEvery { userService.logout(any()) } returns Response.success(Unit)
        coEvery { sessionStorage.clearSession() } returns Unit
        Assert.assertEquals(
            BasicState.SuccessState,
            userDataSource.logout()
        )
    }

    @Test
    fun testLogoutError() = runTest {
        coEvery { sessionStorage.getRefreshToken() } returns REFRESH_TOKEN
        coEvery { userService.logout(any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            BasicState.ErrorState,
            userDataSource.logout()
        )
    }

    @Test
    fun sendFriendRequestSuccessful() = runTest {
        coEvery { userService.sendFriendRequest(any()) } returns Response.success(Unit)
        Assert.assertEquals(
            SendFriendRequestState.SuccessState,
            userDataSource.sendFriendRequest(LOGIN)
        )
    }

    @Test
    fun sendFriendRequestErrorLogin() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 404,
                ERROR to "Not Found",
                MESSAGE to "error"
            )
        )
        coEvery { userService.sendFriendRequest(any()) } returns Response.error(
            404, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.ErrorLoginState, userDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }

    @Test
    fun sendFriendRequestFriendsAlreadyExists() = runTest {
        coEvery { userService.sendFriendRequest(any()) } returns Response.error(
            409, ALREADY_FRIENDS
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.FriendAlreadyExists, userDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }

    @Test
    fun sendFriendRequestSuchRequestAlreadyExists() = runTest {
        val json = JSONObject(
            mapOf(
                STATUS to 409,
                ERROR to "Not Found",
                MESSAGE to "error"
            )
        )
        coEvery { userService.sendFriendRequest(any()) } returns Response.error(
            409, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.SuchRequestAlreadyExists, userDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }

    @Test
    fun sendFriendRequestError() = runTest {
        coEvery { userService.sendFriendRequest(any()) } returns Response.error(
            500, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.ErrorState, userDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }
}
package com.src.book.data.remote.dataSource

import com.src.book.*
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.remote.dataSource.user.UserDataSourceImpl
import com.src.book.data.remote.model.user.userProfile.UserProfileMapper
import com.src.book.data.remote.service.SessionService
import com.src.book.data.remote.service.UserService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.EditProfileState
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
    private lateinit var sessionStorage: SessionStorage

    @MockK
    private lateinit var sessionService: SessionService

    @MockK
    private lateinit var userProfileMapper: UserProfileMapper
    private lateinit var userDataSource: UserDataSource

    @Before
    fun setUp() {
        userDataSource = UserDataSourceImpl(
            userService = userService,
            sessionStorage = sessionStorage,
            sessionService = sessionService,
            userProfileMapper = userProfileMapper
        )
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
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
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
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
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
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
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
        Assert.assertTrue(userDataSource.logout() is BasicState.SuccessState
        )
    }

    @Test
    fun testLogoutError() = runTest {
        coEvery { sessionStorage.getRefreshToken() } returns REFRESH_TOKEN
        coEvery { userService.logout(any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertTrue( userDataSource.logout() is BasicState.ErrorState)
    }

    @Test
    fun testEditProfileSuccessful() = runTest {
        coEvery { userService.editProfile(any(), any()) } returns Response.success(Unit)
        Assert.assertTrue(userDataSource.editProfile("data", null) is EditProfileState.SuccessState)
    }

    @Test
    fun testEditProfileLoginAlreadyExistsError() = runTest {
        coEvery { userService.editProfile(any(), any()) } returns Response.error(
            409, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertTrue(
            userDataSource.editProfile(
                "data",
                null
            ) is EditProfileState.LoginAlreadyExistsState
        )
    }

    @Test
    fun testEditProfileError() = runTest {
        coEvery { userService.editProfile(any(), any()) } returns Response.error(
            500, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertTrue(
            userDataSource.editProfile(
                "data",
                null
            ) is EditProfileState.ErrorState
        )
    }
}
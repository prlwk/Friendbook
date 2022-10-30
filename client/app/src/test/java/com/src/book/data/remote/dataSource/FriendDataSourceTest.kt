package com.src.book.data.remote.dataSource

import com.src.book.ERROR
import com.src.book.LOGIN
import com.src.book.MESSAGE
import com.src.book.STATUS
import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.data.remote.dataSource.friend.FriendDataSourceImpl
import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import com.src.book.data.remote.service.FriendService
import com.src.book.data.remote.utils.ALREADY_FRIENDS
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
import org.junit.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class FriendDataSourceTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var friendService: FriendService

    @MockK
    private lateinit var friendRequestMapper: FriendRequestMapper

    private lateinit var friendDataSource: FriendDataSource

    @Before
    fun setUp() {
        friendDataSource = FriendDataSourceImpl(friendService = friendService, friendRequestMapper = friendRequestMapper)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun sendFriendRequestSuccessful() = runTest {
        coEvery { friendService.sendFriendRequest(any()) } returns Response.success(Unit)
        Assert.assertEquals(
            SendFriendRequestState.SuccessState,
            friendDataSource.sendFriendRequest(LOGIN)
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
        coEvery { friendService.sendFriendRequest(any()) } returns Response.error(
            404, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.ErrorLoginState, friendDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }

    @Test
    fun sendFriendRequestFriendsAlreadyExists() = runTest {
        coEvery { friendService.sendFriendRequest(any()) } returns Response.error(
            409, ALREADY_FRIENDS
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.FriendAlreadyExists, friendDataSource.sendFriendRequest(
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
        coEvery { friendService.sendFriendRequest(any()) } returns Response.error(
            409, json.toString()
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.SuchRequestAlreadyExists, friendDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }

    @Test
    fun sendFriendRequestError() = runTest {
        coEvery { friendService.sendFriendRequest(any()) } returns Response.error(
            500, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertEquals(
            SendFriendRequestState.ErrorState, friendDataSource.sendFriendRequest(
                LOGIN
            )
        )
    }
}
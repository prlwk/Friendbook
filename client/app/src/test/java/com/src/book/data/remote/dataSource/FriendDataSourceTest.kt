package com.src.book.data.remote.dataSource

import com.src.book.*
import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.data.remote.dataSource.friend.FriendDataSourceImpl
import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import com.src.book.data.remote.service.FriendService
import com.src.book.data.remote.utils.ALREADY_FRIENDS
import com.src.book.domain.utils.BasicState
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

    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var friendService: FriendService

    @MockK
    private lateinit var friendRequestMapper: FriendRequestMapper

    private lateinit var friendDataSource: FriendDataSource

    @Before
    fun setUp() {
        friendDataSource = FriendDataSourceImpl(
            friendService = friendService,
            friendRequestMapper = friendRequestMapper
        )
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
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
        val json = JSONObject(
            mapOf(
                STATUS to 409,
                ERROR to "Not Found",
                MESSAGE to ALREADY_FRIENDS
            )
        )
        coEvery { friendService.sendFriendRequest(any()) } returns Response.error(
            409, json.toString()
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

    @Test
    fun getIncomingRequestsSuccessful() = runTest {
        val friendRequestResponseList =
            listOf(testModelsResponseGenerator.generateFriendRequestResponse())
        val friendRequestModel = testModelsGenerator.generateFriendRequestModel()
        coEvery { friendService.getIncomingRequests() } returns Response.success(
            friendRequestResponseList
        )
        coEvery { friendRequestMapper.mapFromResponseToModel(any()) } returns friendRequestModel
        Assert.assertTrue(
            friendDataSource.getIncomingRequests() is BasicState.SuccessStateWithResources<*>
        )
        Assert.assertEquals(
            (friendDataSource.getIncomingRequests() as BasicState.SuccessStateWithResources<*>).data,
            listOf(friendRequestModel)
        )
    }

    @Test
    fun getIncomingRequestsError() = runTest {
        val friendRequestModel = testModelsGenerator.generateFriendRequestModel()
        coEvery { friendService.getIncomingRequests() } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { friendRequestMapper.mapFromResponseToModel(any()) } returns friendRequestModel
        Assert.assertTrue(
            friendDataSource.getIncomingRequests() is BasicState.ErrorState
        )
    }

    @Test
    fun getOutgoingRequestsSuccessful() = runTest {
        val friendRequestResponseList =
            listOf(testModelsResponseGenerator.generateFriendRequestResponse())
        val friendRequestModel = testModelsGenerator.generateFriendRequestModel()
        coEvery { friendService.getOutgoingRequests() } returns Response.success(
            friendRequestResponseList
        )
        coEvery { friendRequestMapper.mapFromResponseToModel(any()) } returns friendRequestModel
        Assert.assertTrue(
            friendDataSource.getOutgoingRequests() is BasicState.SuccessStateWithResources<*>
        )
        Assert.assertEquals(
            (friendDataSource.getOutgoingRequests() as BasicState.SuccessStateWithResources<*>).data,
            listOf(friendRequestModel)
        )
    }

    @Test
    fun getOutgoingRequestsError() = runTest {
        val friendRequestModel = testModelsGenerator.generateFriendRequestModel()
        coEvery { friendService.getOutgoingRequests() } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { friendRequestMapper.mapFromResponseToModel(any()) } returns friendRequestModel
        Assert.assertTrue(
            friendDataSource.getOutgoingRequests() is BasicState.ErrorState
        )
    }

    @Test
    fun submitFriendRequestSuccessful() = runTest {
        coEvery { friendService.submitFriendRequest(any()) } returns Response.success(
            Unit
        )
        Assert.assertTrue(friendDataSource.submitFriendRequest(ID) is BasicState.SuccessState)
    }

    @Test
    fun submitFriendRequestError() = runTest {
        coEvery { friendService.submitFriendRequest(any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertTrue(friendDataSource.submitFriendRequest(ID) is BasicState.ErrorState)
    }

    @Test
    fun rejectIncomingRequestSuccessful() = runTest {
        coEvery { friendService.rejectIncomingFriendRequest(any()) } returns Response.success(
            Unit
        )
        Assert.assertTrue(friendDataSource.rejectIncomingFriendRequest(ID) is BasicState.SuccessState)
    }

    @Test
    fun rejectIncomingRequestError() = runTest {
        coEvery { friendService.rejectIncomingFriendRequest(any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertTrue(friendDataSource.rejectIncomingFriendRequest(ID) is BasicState.ErrorState)
    }

    @Test
    fun rejectOutgoingRequestSuccessful() = runTest {
        coEvery { friendService.rejectOutgoingFriendRequest(any()) } returns Response.success(
            Unit
        )
        Assert.assertTrue(friendDataSource.rejectOutgoingFriendRequest(ID) is BasicState.SuccessState)
    }

    @Test
    fun rejectOutgoingRequestError() = runTest {
        coEvery { friendService.rejectOutgoingFriendRequest(any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        Assert.assertTrue(friendDataSource.rejectOutgoingFriendRequest(ID) is BasicState.ErrorState)
    }
}
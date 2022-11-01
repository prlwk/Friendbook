package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FriendRequestMapperTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var friendRequestMapper: FriendRequestMapper

    @Before
    fun setUp() {
        friendRequestMapper = FriendRequestMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapFriendRequestSuccessful() = runTest {
        val model = testModelsGenerator.generateFriendRequestModel()
        val modelResponse = testModelsResponseGenerator.generateFriendRequestResponse()
        Assert.assertEquals(model, friendRequestMapper.mapFromResponseToModel(modelResponse))
    }

}
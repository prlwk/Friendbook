package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.friend.friend.FriendMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class FriendMapperTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var friendMapper: FriendMapper

    @Before
    fun setUp() {
        friendMapper = FriendMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapFriendRequestSuccessful() = runTest {
        val model = testModelsGenerator.generateFriendModel()
        val modelResponse = testModelsResponseGenerator.generateFriendResponse()
        Assert.assertEquals(model, friendMapper.mapFromResponseToModel(modelResponse))
    }
}
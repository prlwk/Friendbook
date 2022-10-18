package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.tag.TagMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class TagMapperTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var tagMapper: TagMapper

    @Before
    fun setUp() {
        tagMapper = TagMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapTagSuccessful() = runTest {
        val tagResponseModel = testModelsResponseGenerator.generateTagResponseModel()
        val tagModel = testModelsGenerator.generateTagModel()
        Assert.assertEquals(tagModel, tagMapper.mapFromResponseToModel(tagResponseModel))
    }
}
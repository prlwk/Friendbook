package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class BookAuthorMapperTest {
    @get:Rule
    val rule = MockKRule(this)

    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var bookAuthorMapper: BookAuthorMapper

    @Before
    fun setUp() {
        bookAuthorMapper = BookAuthorMapper()

        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapBookAuthorSuccessful() = runTest {
        val bookAuthorResponseModel = testModelsResponseGenerator.generateBookAuthorResponseModel()
        val bookAuthorModel = testModelsGenerator.generateBookAuthorModel()
        Assert.assertEquals(
            bookAuthorModel,
            bookAuthorMapper.mapFromResponseToModel(bookAuthorResponseModel)
        )
    }
}
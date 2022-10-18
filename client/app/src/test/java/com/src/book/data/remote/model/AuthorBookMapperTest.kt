package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class AuthorBookMapperTest {
    @get:Rule
    val rule = MockKRule(this)

    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var authorBookMapper: AuthorBookMapper

    @Before
    fun setUp() {
        authorBookMapper = AuthorBookMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapAuthorSuccessful() = runTest {
        val authorBookModel = testModelsGenerator.generateAuthorBookModel()
        val authorBookResponseModel = testModelsResponseGenerator.generateAuthorBookResponseModel()
        Assert.assertEquals(
            authorBookModel,
            authorBookMapper.mapFromResponseToModel(authorBookResponseModel)
        )
    }
}
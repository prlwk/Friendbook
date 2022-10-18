package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.author.author.AuthorMapper
import com.src.book.data.remote.model.book.bookAuthor.BookAuthorMapper
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
class AuthorMapperTest {
    @get:Rule
    val rule = MockKRule(this)

    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var bookAuthorMapper: BookAuthorMapper
    private lateinit var authorMapper: AuthorMapper

    @Before
    fun setUp() {
        authorMapper = AuthorMapper(bookAuthorMapper)
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapAuthorSuccessful() = runTest {
        val authorModel = testModelsGenerator.generateAuthorModel()
        val authorResponseModel = testModelsResponseGenerator.generateAuthorResponseModel()
        coEvery { bookAuthorMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateBookAuthorModel()
        Assert.assertEquals(
            authorModel,
            authorMapper.mapFromResponseToModel(authorResponseModel)
        )
    }
}
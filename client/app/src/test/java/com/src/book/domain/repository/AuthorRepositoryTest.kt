package com.src.book.domain.repository

import com.src.book.TestModelsGenerator
import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.repository.AuthorRepositoryImpl
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class AuthorRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var authorDataSource: AuthorDataSource
    private lateinit var authorRepository: AuthorRepository

    @Before
    fun setUp() {
        authorRepository = AuthorRepositoryImpl(authorDataSource)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetAuthorByIdSuccessful() = runTest {
        val authorModel = testModelsGenerator.generateAuthorModel()
        coEvery { authorDataSource.loadAuthorById(any()) } returns authorModel
        Assert.assertEquals(
            authorModel,
            authorRepository.getAuthorById(1)
        )
    }
}
package com.src.book.data.remote.dataSource

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.remote.dataSource.author.AuthorDataSourceImpl
import com.src.book.data.remote.model.author.author.AuthorMapper
import com.src.book.data.remote.service.AuthorService
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthorDataSourceTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var authorService: AuthorService

    @MockK
    private lateinit var authorMapper: AuthorMapper
    private lateinit var authorDataSource: AuthorDataSource


    @Before
    fun setUp() {
        authorDataSource = AuthorDataSourceImpl(authorService, authorMapper)
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetAuthorByIdSuccessful() = runTest {
        val authorResponseModel = testModelsResponseGenerator.generateAuthorResponseModel()
        val authorModel = testModelsGenerator.generateAuthorModel()
        coEvery { authorService.getAuthorById(any()) } returns Response.success(authorResponseModel)
        coEvery { authorMapper.mapFromResponseToModel(any()) } returns authorModel
        Assert.assertEquals(
            authorModel,
            authorDataSource.loadAuthorById(1)
        )
    }

    @Test
    fun testGetAuthorByIdFailed() = runTest {
        val authorModel = testModelsGenerator.generateAuthorModel()
        coEvery { authorService.getAuthorById(any()) } returns Response.error(
            404, ResponseBody.create(
                "application/json".toMediaTypeOrNull(), "error"
            )
        )
        coEvery { authorMapper.mapFromResponseToModel(any()) } returns authorModel
        Assert.assertNull(
            authorDataSource.loadAuthorById(1)
        )
    }
}
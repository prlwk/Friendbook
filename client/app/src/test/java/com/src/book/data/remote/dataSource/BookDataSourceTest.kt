package com.src.book.data.remote.dataSource

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.book.BookDataSourceImpl
import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.service.BookService
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
class BookDataSourceTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bookMapper: BookMapper
    private lateinit var bookDataSource: BookDataSource

    @Before
    fun setUp() {
        bookDataSource = BookDataSourceImpl(bookService, bookMapper)
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetBookByIdSuccessful() = runTest {
        val bookResponseModel = testModelsResponseGenerator.generateBookResponseModel()
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookService.getBookById(any()) } returns Response.success(bookResponseModel)
        coEvery { bookMapper.mapFromResponseToModel(any()) } returns bookModel
        Assert.assertEquals(
            bookModel,
            bookDataSource.loadBookById(1)
        )
    }

    @Test
    fun testGetBookByIdFailed() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookService.getBookById(any()) } returns Response.error(
            404, ResponseBody.create(
                "application/json".toMediaTypeOrNull(), "error"
            )
        )
        coEvery { bookMapper.mapFromResponseToModel(any()) } returns bookModel
        Assert.assertNull(
            bookDataSource.loadBookById(1)
        )
    }

    @Test
    fun testGetBooksByAuthorIdSuccessful() = runTest {
        val booksResponseModel = listOf(testModelsResponseGenerator.generateBookResponseModel())
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookService.getAllBooksByAuthorId(any()) } returns Response.success(
            booksResponseModel
        )
        coEvery { bookMapper.mapFromResponseToModel(any()) } returns bookModel
        Assert.assertEquals(
            listOf(bookModel),
            bookDataSource.loadBooksByAuthorId(1)
        )
    }

    @Test
    fun testGetBooksByAuthorIdFailed() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookService.getAllBooksByAuthorId(any()) } returns Response.error(
            404, ResponseBody.create(
                "application/json".toMediaTypeOrNull(), "error"
            )
        )
        coEvery { bookMapper.mapFromResponseToModel(any()) } returns bookModel
        Assert.assertNull(
            bookDataSource.loadBooksByAuthorId(1)
        )
    }
}
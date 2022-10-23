package com.src.book.data.remote.dataSource

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.book.BookDataSourceImpl
import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.tag.TagMapper
import com.src.book.data.remote.service.BookService
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
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

    @MockK
    private lateinit var genreMapper: GenreMapper

    @MockK
    private lateinit var tagMapper: TagMapper
    private lateinit var bookDataSource: BookDataSource

    @Before
    fun setUp() {
        bookDataSource = BookDataSourceImpl(bookService, bookMapper, genreMapper, tagMapper)
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
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
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
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { bookMapper.mapFromResponseToModel(any()) } returns bookModel
        Assert.assertNull(
            bookDataSource.loadBooksByAuthorId(1)
        )
    }

    @Test
    fun getAllGenresSuccessful() = runTest {
        val genreModel = testModelsGenerator.generateGenreModel()
        val genresResponseModel = listOf(testModelsResponseGenerator.generateGenreResponseModel())
        coEvery { bookService.getAllGenres() } returns Response.success(genresResponseModel)
        coEvery { genreMapper.mapFromResponseToModel(any()) } returns genreModel
        Assert.assertEquals(
            listOf(genreModel),
            bookDataSource.loadAllGenres()
        )
    }

    @Test
    fun getAllGenresFailed() = runTest {
        val genreModel = testModelsGenerator.generateGenreModel()
        coEvery { bookService.getAllGenres() } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { genreMapper.mapFromResponseToModel(any()) } returns genreModel
        Assert.assertNull(
            bookDataSource.loadAllGenres()
        )
    }

    @Test
    fun getAllTagsSuccessful() = runTest {
        val tagModel = testModelsGenerator.generateTagModel()
        val tagsResponseModel = listOf(testModelsResponseGenerator.generateTagResponseModel())
        coEvery { bookService.getAllTags() } returns Response.success(tagsResponseModel)
        coEvery { tagMapper.mapFromResponseToModel(any()) } returns tagModel
        Assert.assertEquals(
            listOf(tagModel),
            bookDataSource.loadAllTags()
        )
    }

    @Test
    fun getAllTagsFailed() = runTest {
        val tagModel = testModelsGenerator.generateTagModel()
        coEvery { bookService.getAllGenres() } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { tagMapper.mapFromResponseToModel(any()) } returns tagModel
        Assert.assertNull(
            bookDataSource.loadAllTags()
        )
    }
}
package com.src.book.data.remote.dataSource

import com.src.book.ACCESS_TOKEN
import com.src.book.ID
import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.book.BookDataSourceImpl
import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.model.book.bookList.BookListMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.tag.TagMapper
import com.src.book.data.remote.service.BookService
import com.src.book.data.remote.service.SessionService
import com.src.book.data.remote.session.SessionStorage
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.BookmarkState
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

    @MockK
    private lateinit var sessionService: SessionService

    @MockK
    private lateinit var sessionStorage: SessionStorage

    @MockK
    private lateinit var bookListMapper: BookListMapper
    private lateinit var bookDataSource: BookDataSource

    @Before
    fun setUp() {
        bookDataSource = BookDataSourceImpl(
            bookService = bookService,
            bookMapper = bookMapper,
            genreMapper = genreMapper,
            tagMapper = tagMapper,
            sessionService = sessionService,
            sessionStorage = sessionStorage,
            bookListMapper = bookListMapper
        )
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
            bookDataSource.loadBookById(ID)
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
            bookDataSource.loadBookById(ID)
        )
    }

    @Test
    fun testGetBooksByAuthorIdSuccessfulNotAuth() = runTest {
        val bookListResponseModel =
            listOf(testModelsResponseGenerator.generateBookListResponseModel())
        val bookListModel = testModelsGenerator.generateBookListModel(false)
        coEvery { bookService.getAllBooksByAuthorId(any(), any()) } returns Response.success(
            bookListResponseModel
        )
        coEvery { bookListMapper.mapFromResponseToModel(any(), any()) } returns bookListModel
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        Assert.assertTrue(
            bookDataSource.loadBooksByAuthorId(ID) is BasicState.SuccessState<*>
        )
        Assert.assertEquals(
            (bookDataSource.loadBooksByAuthorId(ID) as BasicState.SuccessState<*>).data,
            listOf(bookListModel)
        )
    }

    @Test
    fun testGetBooksByAuthorIdSuccessfulAuth() = runTest {
        val bookListResponseModel =
            listOf(testModelsResponseGenerator.generateBookListResponseModel())
        val bookListModel = testModelsGenerator.generateBookListModel(true)
        coEvery { bookService.getAllBooksByAuthorId(any(), any()) } returns Response.success(
            bookListResponseModel
        )
        coEvery { bookListMapper.mapFromResponseToModel(any(), any()) } returns bookListModel
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        Assert.assertTrue(
            bookDataSource.loadBooksByAuthorId(ID) is BasicState.SuccessState<*>
        )
        Assert.assertEquals(
            (bookDataSource.loadBooksByAuthorId(ID) as BasicState.SuccessState<*>).data,
            listOf(bookListModel)
        )
    }

    @Test
    fun testGetBooksByAuthorIdEmptyList() = runTest {
        val bookListModel = testModelsGenerator.generateBookListModel(false)
        coEvery { bookService.getAllBooksByAuthorId(any(), any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        coEvery { bookListMapper.mapFromResponseToModel(any(), any()) } returns bookListModel
        Assert.assertTrue(
            bookDataSource.loadBooksByAuthorId(ID) is BasicState.EmptyState
        )
    }

    @Test
    fun testGetBooksByAuthorIdFailed() = runTest {
        val bookListModel = testModelsGenerator.generateBookListModel(false)
        coEvery { bookService.getAllBooksByAuthorId(any(), any()) } returns Response.error(
            403, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        coEvery { bookListMapper.mapFromResponseToModel(any(), any()) } returns bookListModel
        Assert.assertTrue(
            bookDataSource.loadBooksByAuthorId(ID) is BasicState.ErrorState
        )
    }

    @Test
    fun getAllGenresSuccessful() = runTest {
        val genreModel = testModelsGenerator.generateGenreModel()
        val genresResponseModel = listOf(testModelsResponseGenerator.generateGenreResponseModel())
        coEvery { bookService.getAllGenres() } returns Response.success(genresResponseModel)
        coEvery { genreMapper.mapFromResponseToModel(any()) } returns genreModel
        Assert.assertTrue(bookDataSource.loadAllGenres() is BasicState.SuccessState)
        Assert.assertEquals(
            listOf(genreModel),
            (bookDataSource.loadAllGenres() as BasicState.SuccessState).data
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
        Assert.assertTrue(bookDataSource.loadAllGenres() is BasicState.ErrorState)
    }

    @Test
    fun getAllTagsSuccessful() = runTest {
        val tagModel = testModelsGenerator.generateTagModel()
        val tagsResponseModel = listOf(testModelsResponseGenerator.generateTagResponseModel())
        coEvery { bookService.getAllTags() } returns Response.success(tagsResponseModel)
        coEvery { tagMapper.mapFromResponseToModel(any()) } returns tagModel
        Assert.assertTrue(bookDataSource.loadAllTags() is BasicState.SuccessState)
        Assert.assertEquals(
            listOf(tagModel),
            (bookDataSource.loadAllTags() as BasicState.SuccessState).data
        )
    }

    @Test
    fun getAllTagsFailed() = runTest {
        val tagModel = testModelsGenerator.generateTagModel()
        coEvery { bookService.getAllTags() } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { tagMapper.mapFromResponseToModel(any()) } returns tagModel
        Assert.assertTrue(bookDataSource.loadAllTags() is BasicState.ErrorState)
    }

    @Test
    fun testRemoveBookmarkSuccessful() = runTest {
        coEvery { bookService.removeBookmark(any(), any()) } returns Response.success(Unit)
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        Assert.assertTrue(bookDataSource.removeBookmark(ID) is BookmarkState.SuccessState)
    }

    @Test
    fun testRemoveBookmarkError() = runTest {
        coEvery { bookService.removeBookmark(any(), any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        Assert.assertTrue(bookDataSource.removeBookmark(ID) is BookmarkState.ErrorState)
    }

    @Test
    fun testRemoveBookmarkNotAuthorizedError() = runTest {
        coEvery { bookService.removeBookmark(any(), any()) } returns Response.error(
            403, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ""
        Assert.assertTrue(bookDataSource.removeBookmark(ID) is BookmarkState.NotAuthorizedState)
    }

    @Test
    fun testAddBookmarkSuccessful() = runTest {
        coEvery { bookService.addBookmark(any(), any()) } returns Response.success(Unit)
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.SuccessState)
    }

    @Test
    fun testAddBookmarkError() = runTest {
        coEvery { bookService.addBookmark(any(), any()) } returns Response.error(
            404, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ACCESS_TOKEN
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.ErrorState)
    }

    @Test
    fun testAddBookmarkNotAuthorizedError() = runTest {
        coEvery { bookService.addBookmark(any(), any()) } returns Response.error(
            403, "error"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
        coEvery { sessionStorage.accessTokenIsValid() } returns true
        coEvery { sessionStorage.refreshTokenIsValid() } returns true
        coEvery { sessionStorage.getAccessToken() } returns ""
        Assert.assertTrue(bookDataSource.addBookmark(ID) is BookmarkState.NotAuthorizedState)
    }
}
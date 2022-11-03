package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import com.src.book.data.remote.model.book.bookList.BookListMapper
import com.src.book.data.remote.model.genre.GenreMapper
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class BookListMapperTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var authorBookMapper: AuthorBookMapper

    @MockK
    private lateinit var genreMapper: GenreMapper
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var bookListMapper: BookListMapper


    @Before
    fun setUp() {
        bookListMapper =
            BookListMapper(genreMapper = genreMapper, authorBookMapper = authorBookMapper)

        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapSuccessful() = runTest {
        val bookListResponseModel = testModelsResponseGenerator.generateBookListResponseModel()
        val bookListModel = testModelsGenerator.generateBookListModel(false)
        coEvery { genreMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateGenreModel()
        coEvery { authorBookMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateAuthorBookModel()
        Assert.assertEquals(
            bookListModel,
            bookListMapper.mapFromResponseToModel(bookListResponseModel, false)
        )
    }
}
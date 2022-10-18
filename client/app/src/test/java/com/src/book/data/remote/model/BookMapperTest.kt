package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.author.authorBook.AuthorBookMapper
import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.review.reviewBook.ReviewBookMapper
import com.src.book.data.remote.model.tag.TagMapper
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class BookMapperTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var tagMapper: TagMapper

    @MockK
    private lateinit var reviewBookMapper: ReviewBookMapper

    @MockK
    private lateinit var genreMapper: GenreMapper

    @MockK
    private lateinit var authorBookMapper: AuthorBookMapper
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var bookMapper: BookMapper

    @Before
    fun setUp() {
        bookMapper = BookMapper(
            tagMapper = tagMapper,
            reviewBookMapper = reviewBookMapper,
            genreMapper = genreMapper,
            authorBookMapper = authorBookMapper
        )
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapAuthorSuccessful() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        val bookResponseModel = testModelsResponseGenerator.generateBookResponseModel()
        coEvery { tagMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateTagModel()
        coEvery { reviewBookMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateReviewModel()
        coEvery { genreMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateGenreModel()
        coEvery { authorBookMapper.mapFromResponseToModel(any()) } returns testModelsGenerator.generateAuthorBookModel()
        Assert.assertEquals(bookModel, bookMapper.mapFromResponseToModel(bookResponseModel))
    }
}
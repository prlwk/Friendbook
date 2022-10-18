package com.src.book.domain.repository

import com.src.book.TestModelsGenerator
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.repository.BookRepositoryImpl
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class BookRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsGenerator: TestModelsGenerator

    @MockK
    private lateinit var bookDataSource: BookDataSource
    private lateinit var bookRepository: BookRepository

    @Before
    fun setUp() {
        bookRepository = BookRepositoryImpl(bookDataSource)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetBookByIdSuccessful() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookDataSource.loadBookById(any()) } returns bookModel
        Assert.assertEquals(
            bookModel,
            bookRepository.getBookById(1)
        )
    }

    @Test
    fun testGetBooksByAuthorByIdSuccessful() = runTest {
        val booksModel = testModelsGenerator.generateListOfBooksModel()
        coEvery { bookDataSource.loadBooksByAuthorId(any()) } returns booksModel
        Assert.assertEquals(
            booksModel,
            bookRepository.getBooksByAuthorId(1)
        )
    }
}
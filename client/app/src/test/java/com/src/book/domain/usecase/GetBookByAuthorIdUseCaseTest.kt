package com.src.book.domain.usecase

import com.src.book.TestModelsGenerator
import com.src.book.domain.repository.BookRepository
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class GetBookByAuthorIdUseCaseTest {

    @get:Rule
    val rule = MockKRule(this)
    private lateinit var bookRepository: BookRepository
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var getBookByAuthorIdUseCase: GetBooksByAuthorIdUseCase

    @Before
    fun setUp() {
        bookRepository = mockk()
        getBookByAuthorIdUseCase = GetBooksByAuthorIdUseCase(bookRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetBookByIdUseCaseSuccessful() = runTest {
        val booksModel = testModelsGenerator.generateListOfBooksModel()
        coEvery { bookRepository.getBooksByAuthorId(any()) } returns booksModel
        Assert.assertEquals(booksModel, getBookByAuthorIdUseCase.execute(1))
    }
}
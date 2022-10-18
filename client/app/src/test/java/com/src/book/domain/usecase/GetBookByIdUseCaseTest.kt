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
class GetBookByIdUseCaseTest {

    @get:Rule
    val rule = MockKRule(this)
    private lateinit var bookRepository: BookRepository
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var getBookByIdUseCase: GetBookByIdUseCase

    @Before
    fun setUp() {
        bookRepository = mockk()
        getBookByIdUseCase = GetBookByIdUseCase(bookRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetBookByIdUseCaseSuccessful() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { bookRepository.getBookById(any()) } returns bookModel
        Assert.assertEquals(bookModel, getBookByIdUseCase.execute(1))
    }
}
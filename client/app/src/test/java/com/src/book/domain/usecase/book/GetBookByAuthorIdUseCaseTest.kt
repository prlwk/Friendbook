package com.src.book.domain.usecase.book

import com.src.book.ID
import com.src.book.TestModelsGenerator
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
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
        val state = BasicState.SuccessStateWithResources(booksModel)
        coEvery { bookRepository.getBooksByAuthorId(any()) } returns state
        Assert.assertTrue(getBookByAuthorIdUseCase.execute(ID) is BasicState.SuccessStateWithResources<*>)
        Assert.assertEquals(
            (getBookByAuthorIdUseCase.execute(ID) as BasicState.SuccessStateWithResources<*>).data,
        booksModel)
    }

    @Test
    fun testGetBookByIdUseCaseError() = runTest {
        coEvery { bookRepository.getBooksByAuthorId(any()) } returns BasicState.ErrorState
        Assert.assertTrue(getBookByAuthorIdUseCase.execute(ID) is BasicState.ErrorState)
    }
}
package com.src.book.presentation.author

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.GetBooksByAuthorIdUseCase
import com.src.book.presentation.author.list_of_books.ListOfBooksState
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class ListOfBooksViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var listOfBooksViewModel: ListOfBooksViewModel
    private lateinit var getBooksByAuthorIdUseCase: GetBooksByAuthorIdUseCase
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getBooksByAuthorIdUseCase = mockk()
        listOfBooksViewModel =
            ListOfBooksViewModel(getBooksByAuthorIdUseCase = getBooksByAuthorIdUseCase)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadBooksByAuthorIdSuccessful() = runTest {
        val booksModel = listOf(testModelsGenerator.generateBookModel())
        coEvery { getBooksByAuthorIdUseCase.execute(any()) } returns booksModel
        listOfBooksViewModel.loadBooksByAuthorId(1)
        Assert.assertTrue(
            listOfBooksViewModel.liveDataBooks.value is ListOfBooksState.DefaultState
        )
        Assert.assertEquals(
            (listOfBooksViewModel.liveDataBooks.value as ListOfBooksState.DefaultState).books,
            booksModel
        )
    }

    @Test
    fun testLoadBooksByAuthorIdError() = runTest {
        coEvery { getBooksByAuthorIdUseCase.execute(any()) } returns null
        listOfBooksViewModel.loadBooksByAuthorId(1)
        Assert.assertTrue(
            listOfBooksViewModel.liveDataBooks.value is ListOfBooksState.ErrorState
        )
        Assert.assertEquals(
            (listOfBooksViewModel.liveDataBooks.value as ListOfBooksState.ErrorState).books,
            null
        )
    }
}
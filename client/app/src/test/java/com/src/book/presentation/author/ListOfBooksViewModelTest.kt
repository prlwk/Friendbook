package com.src.book.presentation.author

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.ID
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.book.GetBooksByAuthorIdUseCase
import com.src.book.domain.usecase.book.SetBookmarkUseCase
import com.src.book.domain.utils.BasicState
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
    private lateinit var setBookmarkUseCase: SetBookmarkUseCase
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getBooksByAuthorIdUseCase = mockk()
        setBookmarkUseCase = mockk()
        listOfBooksViewModel =
            ListOfBooksViewModel(
                getBooksByAuthorIdUseCase = getBooksByAuthorIdUseCase,
                setBookmarkUseCase = setBookmarkUseCase
            )
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadBooksByAuthorIdSuccessful() = runTest {
        val booksModel = listOf(testModelsGenerator.generateBookModel())
        val state = BasicState.SuccessStateWithResources(booksModel)
        coEvery { getBooksByAuthorIdUseCase.execute(any()) } returns state
        listOfBooksViewModel.loadBooksByAuthorId(ID)
        Assert.assertTrue(
            listOfBooksViewModel.liveDataBooks.value is ListOfBooksState.SuccessState
        )
        Assert.assertEquals(
            (listOfBooksViewModel.liveDataBooks.value as ListOfBooksState.SuccessState).books,
            booksModel
        )
    }

    @Test
    fun testLoadBooksByAuthorIdError() = runTest {
        coEvery { getBooksByAuthorIdUseCase.execute(any()) } returns BasicState.ErrorState
        listOfBooksViewModel.loadBooksByAuthorId(ID)
        Assert.assertTrue(
            listOfBooksViewModel.liveDataBooks.value is ListOfBooksState.ErrorState
        )
    }
}
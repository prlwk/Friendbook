package com.src.book.presentation.book

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.book.GetBookByIdUseCase
import com.src.book.presentation.book.main_page.BookState
import com.src.book.presentation.book.main_page.viewModel.BookViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BookViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var getBookByIdUseCase: GetBookByIdUseCase
    private lateinit var bookViewModel: BookViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getBookByIdUseCase = mockk()
        bookViewModel = BookViewModel(getBookByIdUseCase = getBookByIdUseCase)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetBookByIdSuccessful() = runTest {
        val bookModel = testModelsGenerator.generateBookModel()
        coEvery { getBookByIdUseCase.execute(any()) } returns bookModel
        bookViewModel.loadBookById(1)
        Assert.assertTrue(
            bookViewModel.liveDataBook.value is BookState.DefaultState
        )
        Assert.assertEquals(
            (bookViewModel.liveDataBook.value as BookState.DefaultState).book,
            bookModel
        )
    }

    @Test
    fun testGetBookByIdError() = runTest {
        coEvery { getBookByIdUseCase.execute(any()) } returns null
        bookViewModel.loadBookById(1)
        Assert.assertTrue(
            bookViewModel.liveDataBook.value is BookState.ErrorState
        )
        Assert.assertEquals(
            (bookViewModel.liveDataBook.value as BookState.ErrorState).book,
            null
        )
    }
}
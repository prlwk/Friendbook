package com.src.book.presentation.author

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.src.book.TestModelsGenerator
import com.src.book.domain.usecase.GetAuthorUseCase
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModel
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
class AuthorViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var getAuthorUseCase: GetAuthorUseCase
    private lateinit var authorViewModel: AuthorViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var testModelsGenerator: TestModelsGenerator

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        getAuthorUseCase = mockk()
        authorViewModel = AuthorViewModel(getAuthorUseCase = getAuthorUseCase)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadAuthorByIdSuccessful() = runTest {
        val authorModel = testModelsGenerator.generateAuthorModel()
        coEvery { getAuthorUseCase.execute(any()) } returns authorModel
        authorViewModel.loadAuthorById(1)
        Assert.assertTrue(
            authorViewModel.liveDataAuthor.value is AuthorState.DefaultState
        )
        Assert.assertEquals(
            (authorViewModel.liveDataAuthor.value as AuthorState.DefaultState).author,
            authorModel
        )
    }

    @Test
    fun testLoadAuthorByIdError() = runTest {
        coEvery { getAuthorUseCase.execute(any()) } returns null
        authorViewModel.loadAuthorById(1)
        Assert.assertTrue(
            authorViewModel.liveDataAuthor.value is AuthorState.ErrorState
        )
        Assert.assertEquals(
            (authorViewModel.liveDataAuthor.value as AuthorState.ErrorState).author,
            null
        )
    }
}
package com.src.book.domain.usecase.book

import com.src.book.TestModelsGenerator
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllGenresUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var bookRepository: BookRepository
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var getAllGenresUseCase: GetAllGenresUseCase

    @Before
    fun setUp() {
        bookRepository = mockk()
        getAllGenresUseCase = GetAllGenresUseCase(bookRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetAllGenresUseCaseSuccessful() = runTest {
        val genresModel = listOf(testModelsGenerator.generateGenreModel())
        coEvery { bookRepository.getAllGenres() } returns BasicState.SuccessState(genresModel)
        Assert.assertTrue(getAllGenresUseCase.execute() is BasicState.SuccessState)
        Assert.assertEquals(
            genresModel,
            (getAllGenresUseCase.execute() as BasicState.SuccessState).data
        )
    }

    @Test
    fun testGetAllGenresUseCaseError() = runTest {
        coEvery { bookRepository.getAllGenres() } returns BasicState.ErrorState()
        Assert.assertTrue(getAllGenresUseCase.execute() is BasicState.ErrorState)
    }
}
package com.src.book.domain.usecase.author

import com.src.book.ID
import com.src.book.TestModelsGenerator
import com.src.book.domain.repository.AuthorRepository
import com.src.book.domain.utils.BasicState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert


@ExperimentalCoroutinesApi
class GetAuthorUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var authorRepository: AuthorRepository
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var getAuthorUseCase: GetAuthorUseCase

    @Before
    fun setUp() {
        authorRepository = mockk()

        getAuthorUseCase = GetAuthorUseCase(authorRepository)
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testGetAuthorUseCaseSuccessful() = runTest {
        val authorModel = testModelsGenerator.generateAuthorModel()
        val state = BasicState.SuccessState(authorModel)
        coEvery { authorRepository.getAuthorById(any()) } returns state
        Assert.assertTrue(authorRepository.getAuthorById(ID) is BasicState.SuccessState<*>)
        assertEquals(
            (authorRepository.getAuthorById(ID) as BasicState.SuccessState<*>).data,
            authorModel
        )
    }

    @Test
    fun testGetAuthorUseCaseError() = runTest {
        coEvery { authorRepository.getAuthorById(any()) } returns BasicState.ErrorState()
        Assert.assertTrue(authorRepository.getAuthorById(ID) is BasicState.ErrorState)
    }
}
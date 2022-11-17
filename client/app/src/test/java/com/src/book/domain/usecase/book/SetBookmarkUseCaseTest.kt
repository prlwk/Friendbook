package com.src.book.domain.usecase.book

import com.src.book.ID
import com.src.book.domain.repository.BookRepository
import com.src.book.domain.utils.BookmarkState
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
class SetBookmarkUseCaseTest {

    @get:Rule
    val rule = MockKRule(this)
    private lateinit var bookRepository: BookRepository
    private lateinit var setBookmarkUseCase: SetBookmarkUseCase

    @Before
    fun setUp() {
        bookRepository = mockk()
        setBookmarkUseCase = SetBookmarkUseCase(bookRepository)

    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testExecuteSuccessful() = runTest {
        coEvery { bookRepository.removeBookmark(any()) } returns BookmarkState.SuccessState
        Assert.assertTrue(setBookmarkUseCase.execute(ID, true) is BookmarkState.SuccessState)
    }

    @Test
    fun testExecuteError() = runTest {
        coEvery { bookRepository.removeBookmark(any()) } returns BookmarkState.ErrorState
        Assert.assertTrue(setBookmarkUseCase.execute(ID, true) is BookmarkState.ErrorState)
    }

    @Test
    fun testExecuteNotAuthorizedError() = runTest {
        coEvery { bookRepository.removeBookmark(any()) } returns BookmarkState.NotAuthorizedState
        Assert.assertTrue(setBookmarkUseCase.execute(ID, true) is BookmarkState.NotAuthorizedState)
    }

}
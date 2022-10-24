package com.src.book.domain.usecase.login

import com.src.book.EMAIL
import com.src.book.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class CheckEmailExistsUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var loginRepository: LoginRepository
    private lateinit var checkEmailExistsUseCase: CheckEmailExistsUseCase

    @Before
    fun setUp() {
        checkEmailExistsUseCase = CheckEmailExistsUseCase(loginRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testCheckEmailExistsUseCaseSuccessful() = runTest {
        coEvery { loginRepository.checkEmailExists(any()) } returns false
        Assert.assertEquals(
            false,
            checkEmailExistsUseCase.execute(EMAIL)
        )
    }
}
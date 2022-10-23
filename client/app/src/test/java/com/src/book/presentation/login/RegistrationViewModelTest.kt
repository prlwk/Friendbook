package com.src.book.presentation.login

import com.src.book.EMAIL
import com.src.book.domain.usecase.CheckEmailExistsUseCase
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var checkEmailExistsUseCase: CheckEmailExistsUseCase

    private lateinit var registrationViewModel: RegistrationViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        checkEmailExistsUseCase = mockk()
        registrationViewModel =
            RegistrationViewModel(checkEmailExistsUseCase = checkEmailExistsUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCheckEmailExistsTrueSuccessful() = runTest {
        coEvery { checkEmailExistsUseCase.execute(any()) } returns true
        registrationViewModel.checkEmailExists(EMAIL)
        Assert.assertEquals(
            true, registrationViewModel.liveDataEmailExists.value
        )
        Assert.assertEquals(
            false, registrationViewModel.liveDataIsLoading.value
        )
    }

    @Test
    fun testCheckEmailExistsFalseSuccessful() = runTest {
        coEvery { checkEmailExistsUseCase.execute(any()) } returns false
        registrationViewModel.checkEmailExists(EMAIL)
        Assert.assertEquals(
            false, registrationViewModel.liveDataEmailExists.value
        )
        Assert.assertEquals(
            false, registrationViewModel.liveDataIsLoading.value
        )
    }
}
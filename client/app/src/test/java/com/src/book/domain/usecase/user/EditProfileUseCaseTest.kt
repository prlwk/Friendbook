package com.src.book.domain.usecase.user

import com.src.book.LOGIN
import com.src.book.NAME
import com.src.book.domain.repository.UserRepository
import com.src.book.domain.utils.EditProfileState
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
class EditProfileUseCaseTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var userRepository: UserRepository
    private lateinit var editProfileUseCase: EditProfileUseCase

    @Before
    fun setUp() {
        editProfileUseCase = EditProfileUseCase(userRepository)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun executeSuccessful() = runTest {
        coEvery { userRepository.editProfile(any(), any()) } returns EditProfileState.SuccessState
        Assert.assertTrue(
            editProfileUseCase.execute(
                login = LOGIN,
                name = NAME,
                uri = null
            ) is EditProfileState.SuccessState
        )
    }

    @Test
    fun executeLoginAlreadyExistsError() = runTest {
        coEvery {
            userRepository.editProfile(
                any(),
                any()
            )
        } returns EditProfileState.LoginAlreadyExistsState
        Assert.assertTrue(
            editProfileUseCase.execute(
                login = LOGIN,
                name = NAME,
                uri = null
            ) is EditProfileState.LoginAlreadyExistsState
        )
    }

    @Test
    fun executeError() = runTest {
        coEvery { userRepository.editProfile(any(), any()) } returns EditProfileState.ErrorState
        Assert.assertTrue(
            editProfileUseCase.execute(
                login = LOGIN,
                name = NAME,
                uri = null
            ) is EditProfileState.ErrorState
        )
    }
}
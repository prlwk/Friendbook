package com.src.book.domain.repository

import com.src.book.PASSWORD
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.repository.UserRepositoryImpl
import com.src.book.domain.utils.BasicState
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
class UserRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var userDataSource: UserDataSource
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(userDataSource)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testChangePasswordSuccessful() = runTest {
        val password = PASSWORD
        coEvery { userDataSource.changePassword(any()) } returns BasicState.SuccessState
        Assert.assertEquals(
            BasicState.SuccessState,
            userRepository.changePassword(password)
        )
    }

    @Test
    fun testChangePasswordError() = runTest {
        coEvery { userDataSource.changePassword(any()) } returns BasicState.ErrorState
        Assert.assertEquals(
            BasicState.ErrorState,
            userRepository.changePassword(PASSWORD)
        )
    }
}
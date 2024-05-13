package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.repository.AuthRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify

import org.junit.After
import org.junit.Before
import org.junit.Test

class GetCurrentUserUseCaseTest {
    @MockK
    private lateinit var authRepository: AuthRepository
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    @MockK
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(authRepository)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    @Test
    fun `return user if user is logged in`() {
        every { authRepository.currentUser } returns firebaseUser

        val returnedUser = getCurrentUserUseCase()

        verify(exactly = 1) { authRepository.currentUser }
        assertThat(returnedUser).isEqualTo(firebaseUser)
        assertThat(returnedUser).isInstanceOf(FirebaseUser::class.java)
    }

    @Test
    fun `return null if user is not logged in`() {
        every { authRepository.currentUser } returns null

        val returnedUser = getCurrentUserUseCase()

        verify(exactly = 1) { authRepository.currentUser }
        assertThat(returnedUser).isNull()
    }
}
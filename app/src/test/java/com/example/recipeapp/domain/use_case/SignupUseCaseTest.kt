package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.AuthRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SignupUseCaseTest {

    @MockK
    private lateinit var authRepository: AuthRepository
    private lateinit var signupUseCase: SignupUseCase
    @MockK
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        signupUseCase = SignupUseCase(authRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(authRepository)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    @Test
    fun `return signed up user`() {
        val email = "email@email.com"
        val password = "Qwerty1+"
        val result = Resource.Success(firebaseUser)

        coEvery { authRepository.signup(any(), any()) } returns flowOf(result)

        val response = runBlocking { signupUseCase(email, password).first() }

        coVerify(exactly = 1) { authRepository.signup(email, password) }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isEqualTo(firebaseUser)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        val email = "email@email.com"
        val password = "Qwerty1+"

        coEvery {
            authRepository.signup(any(), any())
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { signupUseCase(email, password).first() }

        coVerify(exactly = 1) { authRepository.signup(email, password) }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `login is loading`() {
        val email = "email@email.com"
        val password = "Qwerty1+"

        coEvery {
            authRepository.signup(any(), any())
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { signupUseCase(email, password).first() }

        coVerify(exactly = 1) { authRepository.signup(email, password) }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}
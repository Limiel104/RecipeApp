package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.repository.UserRepository
import com.google.common.truth.Truth.assertThat
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

class UpdateUserUseCaseTest {

    @MockK
    private lateinit var userRepository: UserRepository
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var user: User

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateUserUseCase = UpdateUserUseCase(userRepository)

        user = User(
            userUID = "userUID",
            name = "John Smith"
        )
    }

    @After
    fun tearDown() {
        confirmVerified(userRepository)
        clearAllMocks()
    }

    @Test
    fun `return true - user updated`() {
        val result = Resource.Success(true)

        coEvery { userRepository.updateUser(any()) } returns flowOf(result)

        val response = runBlocking { updateUserUseCase(user).first() }

        coVerify(exactly = 1) { userRepository.updateUser(user) }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isTrue()
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            userRepository.updateUser(any())
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { updateUserUseCase(user).first() }

        coVerify(exactly = 1) { userRepository.updateUser(user) }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `updateUser is loading`() {
        coEvery {
            userRepository.updateUser(any())
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { updateUserUseCase(user).first() }

        coVerify(exactly = 1) { userRepository.updateUser(user) }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}
package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.repository.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetUserUseCaseTest {

    @MockK
    private lateinit var userRepository: UserRepository
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var user: User


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserUseCase = GetUserUseCase(userRepository)

        user = User(
            userUID = "userUID",
            name = "John Smith"
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `return user`() {
        val result = Resource.Success(user)

        coEvery { userRepository.getUser(any()) } returns flowOf(result)

        val response = runBlocking { getUserUseCase("userUID").first() }

        coVerify(exactly = 1) { userRepository.getUser("userUID") }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isEqualTo(user)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            userRepository.getUser(any())
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getUserUseCase("userUID").first() }

        coVerify(exactly = 1) { userRepository.getUser("userUID") }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getUser is loading`() {
        coEvery {
            userRepository.getUser(any())
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getUserUseCase("userUID").first() }

        coVerify(exactly = 1) { userRepository.getUser("userUID") }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}
package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.AuthRepository
import com.google.common.truth.Truth
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

class UpdateUserPasswordUseCaseTest {

    @MockK
    private lateinit var authRepository: AuthRepository
    private lateinit var updateUserPasswordUseCase: UpdateUserPasswordUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateUserPasswordUseCase = UpdateUserPasswordUseCase(authRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(authRepository)
        clearAllMocks()
    }

    @Test
    fun `get true - password is updated`() {
        val result = Resource.Success(true)

        coEvery { authRepository.updateUserPassword(any()) } returns flowOf(result)

        val response = runBlocking { updateUserPasswordUseCase("newPassword1+").first() }

        coVerify(exactly = 1) { authRepository.updateUserPassword("newPassword1+") }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isTrue()
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery { authRepository.updateUserPassword(any()) } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { updateUserPasswordUseCase("newPassword1+").first() }

        coVerify(exactly = 1) { authRepository.updateUserPassword("newPassword1+") }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `addShoppingList is loading`() {
        coEvery { authRepository.updateUserPassword(any()) } returns flowOf(Resource.Loading(true))

        val response = runBlocking { updateUserPasswordUseCase("newPassword1+").first() }

        coVerify(exactly = 1) { authRepository.updateUserPassword("newPassword1+") }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}
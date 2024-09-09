package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.ShoppingListRepository
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

class DeleteShoppingListUseCaseTest {

    @MockK
    private lateinit var shoppingListRepository: ShoppingListRepository
    private lateinit var deleteShoppingListUseCase: DeleteShoppingListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        deleteShoppingListUseCase = DeleteShoppingListUseCase(shoppingListRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(shoppingListRepository)
        clearAllMocks()
    }

    @Test
    fun `get true - shoppingList deleted`() {
        val result = Resource.Success(true)

        coEvery { shoppingListRepository.deleteShoppingList(any()) } returns flowOf(result)

        val response = runBlocking { deleteShoppingListUseCase("shoppingListId").first() }

        coVerify(exactly = 1) { shoppingListRepository.deleteShoppingList("shoppingListId") }
        Truth.assertThat(response).isEqualTo(result)
        Truth.assertThat(response).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(response.data).isTrue()
        Truth.assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery { shoppingListRepository.deleteShoppingList(any()) } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { deleteShoppingListUseCase("shoppingListId").first() }

        coVerify(exactly = 1) { shoppingListRepository.deleteShoppingList("shoppingListId") }
        Truth.assertThat(response).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `deleteShoppingList is loading`() {
        coEvery { shoppingListRepository.deleteShoppingList(any()) } returns flowOf(Resource.Loading(true))

        val response = runBlocking { deleteShoppingListUseCase("shoppingListId").first() }

        coVerify(exactly = 1) { shoppingListRepository.deleteShoppingList("shoppingListId") }
        Truth.assertThat(response).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(response.data).isNull()
        Truth.assertThat(response.message).isNull()
    }
}
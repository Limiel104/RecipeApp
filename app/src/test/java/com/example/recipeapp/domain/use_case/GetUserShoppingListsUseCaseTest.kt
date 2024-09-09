package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.repository.ShoppingListRepository
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

class GetUserShoppingListsUseCaseTest {

    @MockK
    private lateinit var shoppingListRepository: ShoppingListRepository
    private lateinit var getUserShoppingListsUseCase: GetUserShoppingListsUseCase
    private lateinit var shoppingList: ShoppingList
    private lateinit var shoppingList2: ShoppingList
    private lateinit var shoppingList3: ShoppingList


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserShoppingListsUseCase = GetUserShoppingListsUseCase(shoppingListRepository)

        shoppingList = ShoppingList(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userId",
            date = 1234324254
        )

        shoppingList2 = ShoppingList(
            shoppingListId = "shoppingList2Id",
            name = "Shopping List 2 Name",
            createdBy = "userId",
            date = 1234324374
        )

        shoppingList3 = ShoppingList(
            shoppingListId = "shoppingList3Id",
            name = "Shopping List 3 Name",
            createdBy = "userId",
            date = 1234324354
        )
    }

    @After
    fun tearDown() {
        confirmVerified(shoppingListRepository)
        clearAllMocks()
    }

    @Test
    fun `return list of shoppingLists`() {
        val result = Resource.Success(listOf(shoppingList, shoppingList2, shoppingList3))

        coEvery {
            shoppingListRepository.getUserShoppingLists("userId",false)
        } returns flowOf(result)

        val response = runBlocking { getUserShoppingListsUseCase("userId",false).first() }

        coVerify(exactly = 1) { shoppingListRepository.getUserShoppingLists("userId",false) }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(3)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of shoppingLists - only one shoppingList in the list`() {
        val result = Resource.Success(listOf(shoppingList))

        coEvery {
            shoppingListRepository.getUserShoppingLists("userId",false)
        } returns flowOf(result)

        val response = runBlocking { getUserShoppingListsUseCase("userId",false).first() }

        coVerify(exactly = 1) { shoppingListRepository.getUserShoppingLists("userId",false) }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(1)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return list of shoppingLists - no shoppingLists returned`() {
        val result = Resource.Success(emptyList<ShoppingList>())

        coEvery {
            shoppingListRepository.getUserShoppingLists("userId",false)
        } returns flowOf(result)

        val response = runBlocking { getUserShoppingListsUseCase("userId",false).first() }

        coVerify(exactly = 1) { shoppingListRepository.getUserShoppingLists("userId",false) }
        assertThat(response).isEqualTo(result)
        assertThat(response).isInstanceOf(Resource.Success::class.java)
        assertThat(response.data).isInstanceOf(List::class.java)
        assertThat(response.data).hasSize(0)
        assertThat(response.message).isNull()
    }

    @Test
    fun `return error`() {
        coEvery {
            shoppingListRepository.getUserShoppingLists("userId",false)
        } returns flowOf(Resource.Error("Error message"))

        val response = runBlocking { getUserShoppingListsUseCase("userId",false).first() }

        coVerify(exactly = 1) { shoppingListRepository.getUserShoppingLists("userId",false) }
        assertThat(response).isInstanceOf(Resource.Error::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isEqualTo("Error message")
    }

    @Test
    fun `getUserShoppingLists is loading`() {
        coEvery {
            shoppingListRepository.getUserShoppingLists("userId",false)
        } returns flowOf(Resource.Loading(true))

        val response = runBlocking { getUserShoppingListsUseCase("userId",false).first() }

        coVerify(exactly = 1) { shoppingListRepository.getUserShoppingLists("userId",false) }
        assertThat(response).isInstanceOf(Resource.Loading::class.java)
        assertThat(response.data).isNull()
        assertThat(response.message).isNull()
    }
}
package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.use_case.AddShoppingListUseCase
import com.example.recipeapp.domain.use_case.DeleteShoppingListUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetShoppingListUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.domain.use_case.ValidateNameUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getIngredientsUseCase: GetIngredientsUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var addShoppingListUseCase: AddShoppingListUseCase
    private lateinit var getUserShoppingListsUseCase: GetUserShoppingListsUseCase
    private lateinit var getShoppingListUseCase: GetShoppingListUseCase
    private lateinit var deleteShoppingListUseCase: DeleteShoppingListUseCase
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var ingredients: List<Ingredient>
    private lateinit var shoppingLists: List<ShoppingList>
    private lateinit var emptyShoppingListWithIngredients: ShoppingListWithIngredients
    private lateinit var displayedShoppingList: ShoppingListWithIngredients
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        validateNameUseCase = ValidateNameUseCase()

        getIngredientsUseCase = mockk()
        getCurrentUserUseCase = mockk()
        addShoppingListUseCase = mockk()
        getUserShoppingListsUseCase = mockk()
        getShoppingListUseCase = mockk()
        deleteShoppingListUseCase = mockk()
        firebaseUser = mockk()

        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"

        ingredients = listOf(
            Ingredient(
                ingredientId = "ingredientId",
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient 2 Name",
                imageUrl = "image2Url",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient 3 Name",
                imageUrl = "image3Url",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient4Id",
                name = "Ingredient 4 Name",
                imageUrl = "image4Url",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient5Id",
                name = "Ingredient 5 Name",
                imageUrl = "image5Url",
                category = "category5"
            )
        )

        shoppingLists = listOf(
            ShoppingList(
                shoppingListId = "shoppingListId",
                name = "Shopping List Name",
                createdBy = "userUID",
                date = 1234564398
            ),
            ShoppingList(
                shoppingListId = "shoppingList2Id",
                name = "Shopping List 2 Name",
                createdBy = "userUID",
                date = 1234345349
            ),
            ShoppingList(
                shoppingListId = "shoppingList3Id",
                name = "Shopping List 3 Name",
                createdBy = "userUID",
                date = 1234324345
            ),
            ShoppingList(
                shoppingListId = "shoppingList4Id",
                name = "Shopping List 4 Name",
                createdBy = "userUID",
                date = 1234324357
            ),
            ShoppingList(
                shoppingListId = "shoppingList5Id",
                name = "Shopping List 5 Name",
                createdBy = "userUID",
                date = 1234824354
            )
        )

        emptyShoppingListWithIngredients = ShoppingListWithIngredients(
            shoppingListId = "",
            name = "",
            createdBy = "",
            ingredients = emptyMap(),
            checkedIngredients = emptyMap(),
            date = 0
        )

        displayedShoppingList = ShoppingListWithIngredients(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userUID",
            ingredients = mapOf(
                ingredients[2] to "3 g",
                ingredients[1] to "5 g"
            ),
            checkedIngredients = mapOf(
                ingredients[0] to false,
                ingredients[1] to true,
                ingredients[2] to false,
                ingredients[3] to false,
                ingredients[4] to true
            ),
            date = 1234324354
        )
    }

    @After
    fun tearDown() {
        confirmVerified(getIngredientsUseCase)
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(addShoppingListUseCase)
        confirmVerified(getUserShoppingListsUseCase)
        clearAllMocks()
    }

    private fun setViewModel(): ShoppingListViewModel {
        return ShoppingListViewModel(
            getCurrentUserUseCase,
            getIngredientsUseCase,
            addShoppingListUseCase,
            getUserShoppingListsUseCase,
            getShoppingListUseCase,
            deleteShoppingListUseCase,
            validateNameUseCase
        )
    }

    private fun getCurrentShoppingListState(): ShoppingListState {
        return shoppingListViewModel.shoppingListState.value
    }

    private fun setMocks() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Success(shoppingLists))
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Success(displayedShoppingList))
    }

    private fun setMocksWithEmptyShoppingList() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Success(shoppingLists))
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Success(emptyShoppingListWithIngredients))
    }

    private fun verifyMocks() {
        coVerifySequence {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID",any())
        }
    }

    @Test
    fun `checkIfUserLoggedIn - user is logged in`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().isUserLoggedIn

        verifyMocks()
        assertThat(result).isTrue()
    }

    @Test
    fun `checkIfUserLoggedIn - user is not logged in`() {
        every { getCurrentUserUseCase() } returns null

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().isUserLoggedIn

        verifySequence { getCurrentUserUseCase() }
        assertThat(result).isFalse()
    }

    @Test
    fun `getIngredients runs successfully`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEqualTo(ingredients)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getIngredients returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Error("Error message"))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Success(shoppingLists))
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Success(displayedShoppingList))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getIngredients is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Loading(true))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Success(shoppingLists))
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Success(displayedShoppingList))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `enteredIngredient - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialIngredientState = getCurrentShoppingListState().ingredient

        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredIngredient("ingredient"))
        val resultIngredientState = getCurrentShoppingListState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEmpty()
        assertThat(resultIngredientState).isEqualTo("ingredient")
    }

    @Test
    fun `enteredIngredient - initially not empty - changed string`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredIngredient("old ingredient"))
        val initialIngredientState = getCurrentShoppingListState().ingredient

        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredIngredient("new ingredient"))
        val resultIngredientState = getCurrentShoppingListState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEqualTo("old ingredient")
        assertThat(resultIngredientState).isEqualTo("new ingredient")
    }

    @Test
    fun `enteredIngredient - initially not empty - result empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredIngredient("ingredient"))
        val initialIngredientState = getCurrentShoppingListState().ingredient

        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredIngredient(""))
        val resultIngredientState = getCurrentShoppingListState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEqualTo("ingredient")
        assertThat(resultIngredientState).isEqualTo("")
    }

    @Test
    fun `onAddButtonClicked - state is set correctly`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        val resultAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened

        verifyMocks()
        assertThat(initialAddIngredientsDialogState).isFalse()
        assertThat(resultAddIngredientsDialogState).isTrue()
    }

    @Test
    fun `onAddIngredientDialogDismiss - no selected ingredients - state is set correctly`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        val initialAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val initialSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val initialIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val resultSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val resultIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialAddIngredientsDialogState).isTrue()
        assertThat(initialSelectedIngredientsState).isEmpty()
        assertThat(initialIngredientsToSelect).isEqualTo(ingredients)
        assertThat(resultAddIngredientsDialogState).isFalse()
        assertThat(resultSelectedIngredientsState).isEmpty()
        assertThat(resultIngredientsToSelect).isEqualTo(ingredients)
    }

    @Test
    fun `onAddIngredientDialogDismiss - selected ingredients - state is set correctly`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        val initialAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val initialSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val initialIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val resultSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val resultIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialAddIngredientsDialogState).isTrue()
        assertThat(initialSelectedIngredientsState).isEqualTo(listOf(ingredients[3],ingredients[2]))
        assertThat(initialIngredientsToSelect).isEqualTo(listOf(
            ingredients[0],
            ingredients[1],
            ingredients[4]
        ))
        assertThat(resultAddIngredientsDialogState).isFalse()
        assertThat(resultSelectedIngredientsState).isEmpty()
        assertThat(resultIngredientsToSelect).isEqualTo(ingredients)
    }

    @Test
    fun `onAddIngredientDialogDismiss - selected ingredients before - state is set correctly`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        val initialAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val initialSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val initialIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val resultSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val resultIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialAddIngredientsDialogState).isTrue()
        assertThat(initialSelectedIngredientsState).isEmpty()
        assertThat(initialIngredientsToSelect).isEqualTo(listOf(
            ingredients[0],
            ingredients[1],
            ingredients[4]
        ))
        assertThat(resultAddIngredientsDialogState).isFalse()
        assertThat(resultSelectedIngredientsState).isEmpty()
        assertThat(resultIngredientsToSelect).isEqualTo(listOf(
            ingredients[0],
            ingredients[1],
            ingredients[4]
        ))
    }

    @Test
    fun `onAddIngredientDialogDismiss - selected ingredients before and after - state is set correctly`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        val initialAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val initialSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val initialIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultAddIngredientsDialogState = getCurrentShoppingListState().isAddIngredientsDialogOpened
        val resultSelectedIngredientsState = getCurrentShoppingListState().selectedIngredients
        val resultIngredientsToSelect = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialAddIngredientsDialogState).isTrue()
        assertThat(initialSelectedIngredientsState).isEqualTo(listOf(ingredients[4]))
        assertThat(initialIngredientsToSelect).isEqualTo(listOf(
            ingredients[0],
            ingredients[1]
        ))
        assertThat(resultAddIngredientsDialogState).isFalse()
        assertThat(resultSelectedIngredientsState).isEmpty()
        assertThat(resultIngredientsToSelect).isEqualTo(listOf(
            ingredients[0],
            ingredients[1],
            ingredients[4]
        ))
    }

    @Test
    fun `onAddIngredientDialogDismiss - selected ingredients - shopping list ingredients`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        val initialIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        verifyMocks()
        assertThat(initialIngredientsState).isEmpty()
        assertThat(resultIngredientsState).isEmpty()
    }

    @Test
    fun `onAddIngredientDialogDismiss - selected ingredients before - shopping list ingredients`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        val initialIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[3],""),
            Pair(ingredients[2],"")
        ))
        assertThat(resultIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[3],""),
            Pair(ingredients[2],"")
        ))
    }

    @Test
    fun `onAddIngredientDialogDismiss - selected ingredients before and after - shopping list ingredients`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddButtonClicked)
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        val initialIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss)
        val resultIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[3],""),
            Pair(ingredients[2],"")
        ))
        assertThat(resultIngredientsState).isEqualTo(mapOf(
            Pair(ingredients[3],""),
            Pair(ingredients[2],"")
        ))
    }

    @Test
    fun `onDropDownMenuExpandChange - initially false`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialDropDownMenuExpandState = getCurrentShoppingListState().isDropDownMenuExpanded

        shoppingListViewModel.onEvent(ShoppingListEvent.OnDropDownMenuExpandChange)
        val resultDropDownMenuExpandState = getCurrentShoppingListState().isDropDownMenuExpanded

        verifyMocks()
        assertThat(initialDropDownMenuExpandState).isFalse()
        assertThat(resultDropDownMenuExpandState).isTrue()
    }

    @Test
    fun `onDropDownMenuExpandChange - initially true`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnDropDownMenuExpandChange)
        val initialDropDownMenuExpandState = getCurrentShoppingListState().isDropDownMenuExpanded

        shoppingListViewModel.onEvent(ShoppingListEvent.OnDropDownMenuExpandChange)
        val resultDropDownMenuExpandState = getCurrentShoppingListState().isDropDownMenuExpanded

        verifyMocks()
        assertThat(initialDropDownMenuExpandState).isTrue()
        assertThat(resultDropDownMenuExpandState).isFalse()
    }

    @Test
    fun `selectedIngredient - no ingredients selected - shopping list ingredients`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        val initialShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val resultShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        verifyMocks()
        assertThat(initialShoppingListIngredientsState).isEmpty()
        assertThat(resultShoppingListIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `selectedIngredient - no ingredients selected - ingredients to select`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        val initialIngredientsState = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val resultIngredientsState = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(ingredients)
        assertThat(resultIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[1],
                ingredients[3],
                ingredients[4]
            )
        )
    }

    @Test
    fun `selectedIngredient - 2 out of 5 ingredients selected initially - shopping list ingredients`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val resultShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        verifyMocks()
        assertThat(initialShoppingListIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[4],""),
                Pair(ingredients[1],"")
            )
        )
        assertThat(resultShoppingListIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[4],""),
                Pair(ingredients[1],""),
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `selectedIngredient - 2 out of 5 ingredients selected initially - ingredients to select`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialIngredientsState = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val resultIngredientsState = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[2],
                ingredients[3]
            )
        )
        assertThat(resultIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[3]
            )
        )
    }

    @Test
    fun `selectedIngredient - 4 out of 5 ingredients selected initially - shopping list ingredients`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val resultShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients

        verifyMocks()
        assertThat(initialShoppingListIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[1],""),
                Pair(ingredients[3],"")
            )
        )
        assertThat(resultShoppingListIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[1],""),
                Pair(ingredients[3],""),
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `selectedIngredient - 4 out of 5 ingredients selected initially - ingredients to select`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialIngredientsState = getCurrentShoppingListState().ingredientsToSelect

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val resultIngredientsState = getCurrentShoppingListState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(
            listOf(
                ingredients[2]
            )
        )
        assertThat(resultIngredientsState).isEqualTo(emptyList<Ingredient>())
    }

    @Test
    fun `onIngredientClicked - initially not selected - selected ingredient`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialSelectedIngredientIdState = getCurrentShoppingListState().clickedIngredientId

        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val resultSelectedIngredientIdState = getCurrentShoppingListState().clickedIngredientId

        verifyMocks()
        assertThat(initialSelectedIngredientIdState).isEqualTo("")
        assertThat(resultSelectedIngredientIdState).isEqualTo(ingredients[2].ingredientId)
    }


    @Test
    fun `onIngredientClicked - initially selected - selected ingredient`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[1].ingredientId))
        val initialSelectedIngredientIdState = getCurrentShoppingListState().clickedIngredientId

        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val resultSelectedIngredientIdState = getCurrentShoppingListState().clickedIngredientId

        verifyMocks()
        assertThat(initialSelectedIngredientIdState).isEqualTo(ingredients[1].ingredientId)
        assertThat(resultSelectedIngredientIdState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onIngredientClicked - is quantity bottom sheet opened after click`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialQuantityBottomSheetState = getCurrentShoppingListState().isQuantityBottomSheetOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val resultQuantityBottomSheetState = getCurrentShoppingListState().isQuantityBottomSheetOpened

        verifyMocks()
        assertThat(initialQuantityBottomSheetState).isFalse()
        assertThat(resultQuantityBottomSheetState).isTrue()
    }

    @Test
    fun `selectedWholeQuantity - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialWholeQuantityState = getCurrentShoppingListState().selectedWholeQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("350"))
        val resultWholeQuantityState = getCurrentShoppingListState().selectedWholeQuantity

        verifyMocks()
        assertThat(initialWholeQuantityState).isEqualTo("")
        assertThat(resultWholeQuantityState).isEqualTo("350")
    }

    @Test
    fun `selectedWholeQuantity - initially not empty - changed value`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("350"))
        val initialWholeQuantityState = getCurrentShoppingListState().selectedWholeQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("45"))
        val resultWholeQuantityState = getCurrentShoppingListState().selectedWholeQuantity

        verifyMocks()
        assertThat(initialWholeQuantityState).isEqualTo("350")
        assertThat(resultWholeQuantityState).isEqualTo("45")
    }

    @Test
    fun `selectedWholeQuantity - initially not empty - result empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("350"))
        val initialWholeQuantityState = getCurrentShoppingListState().selectedWholeQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("0"))
        val resultWholeQuantityState = getCurrentShoppingListState().selectedWholeQuantity

        verifyMocks()
        assertThat(initialWholeQuantityState).isEqualTo("350")
        assertThat(resultWholeQuantityState).isEqualTo("0")
    }

    @Test
    fun `selectedDecimalQuantity - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialDecimalQuantityState = getCurrentShoppingListState().selectedDecimalQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".7"))
        val resultDecimalQuantityState = getCurrentShoppingListState().selectedDecimalQuantity

        verifyMocks()
        assertThat(initialDecimalQuantityState).isEqualTo("")
        assertThat(resultDecimalQuantityState).isEqualTo(".7")
    }

    @Test
    fun `selectedDecimalQuantity - initially not empty - changed value`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".7"))
        val initialDecimalQuantityState = getCurrentShoppingListState().selectedDecimalQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".3"))
        val resultDecimalQuantityState = getCurrentShoppingListState().selectedDecimalQuantity

        verifyMocks()
        assertThat(initialDecimalQuantityState).isEqualTo(".7")
        assertThat(resultDecimalQuantityState).isEqualTo(".3")
    }

    @Test
    fun `selectedDecimalQuantity - initially not empty - result empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".7"))
        val initialDecimalQuantityState = getCurrentShoppingListState().selectedDecimalQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        val resultDecimalQuantityState = getCurrentShoppingListState().selectedDecimalQuantity

        verifyMocks()
        assertThat(initialDecimalQuantityState).isEqualTo(".7")
        assertThat(resultDecimalQuantityState).isEqualTo(".0")
    }

    @Test
    fun `selectedTypeQuantity - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialTypeQuantityState = getCurrentShoppingListState().selectedTypeQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("bowl"))
        val resultTypeQuantityState = getCurrentShoppingListState().selectedTypeQuantity

        verifyMocks()
        assertThat(initialTypeQuantityState).isEqualTo("")
        assertThat(resultTypeQuantityState).isEqualTo("bowl")
    }

    @Test
    fun `selectedTypeQuantity - initially not empty - changed value`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("bowl"))
        val initialTypeQuantityState = getCurrentShoppingListState().selectedTypeQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("handful"))
        val resultTypeQuantityState = getCurrentShoppingListState().selectedTypeQuantity

        verifyMocks()
        assertThat(initialTypeQuantityState).isEqualTo("bowl")
        assertThat(resultTypeQuantityState).isEqualTo("handful")
    }

    @Test
    fun `selectedTypeQuantity - initially not empty - result empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("bowl"))
        val initialTypeQuantityState = getCurrentShoppingListState().selectedTypeQuantity

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("-"))
        val resultTypeQuantityState = getCurrentShoppingListState().selectedTypeQuantity

        verifyMocks()
        assertThat(initialTypeQuantityState).isEqualTo("bowl")
        assertThat(resultTypeQuantityState).isEqualTo("-")
    }

    @Test
    fun `onQuantityPickerSaved - isQuantityBottomSheetOpened state is set correctly`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialQuantityBottomSheetState = getCurrentShoppingListState().isQuantityBottomSheetOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultQuantityBottomSheetState = getCurrentShoppingListState().isQuantityBottomSheetOpened

        verifyMocks()
        assertThat(initialQuantityBottomSheetState).isTrue()
        assertThat(resultQuantityBottomSheetState).isFalse()
    }

    @Test
    fun `onQuantityPickerSaved - quantity is not selected`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerSaved - quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5 g")
    }

    @Test
    fun `onQuantityPickerSaved - whole and decimal quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5")
    }

    @Test
    fun `onQuantityPickerSaved - whole and type quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30 g")
    }

    @Test
    fun `onQuantityPickerSaved - decimal and type quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo(".5 g")
    }

    @Test
    fun `onQuantityPickerSaved - only whole quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30")
    }

    @Test
    fun `onQuantityPickerSaved - only decimal quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo(".5")
    }

    @Test
    fun `onQuantityPickerSaved - only type quantity is selected - initially empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("g")
    }

    @Test
    fun `onQuantityPickerSaved - quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("12"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("kg"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12.0 kg")
    }

    @Test
    fun `onQuantityPickerSaved - whole and decimal quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("12"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12.0")
    }

    @Test
    fun `onQuantityPickerSaved - whole and type quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("12"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("kg"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12 kg")
    }

    @Test
    fun `onQuantityPickerSaved - decimal and type quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("kg"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo(".0 kg")
    }

    @Test
    fun `onQuantityPickerSaved - only whole quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("12"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12")
    }

    @Test
    fun `onQuantityPickerSaved - only decimal quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo(".0")
    }

    @Test
    fun `onQuantityPickerSaved - only type quantity is selected - initially not empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("kg"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("kg")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity not set - still default`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity set - value not changed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5 g")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity not set and then changed - still default`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("15"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("kg"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity set and then changed - value not changed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("30"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".5"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("g"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity("15"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(".0"))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity("kg"))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentShoppingListState().shoppingListIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5 g")
    }

    @Test
    fun `onQuantityPickerDismissed - quantity bottom sheet is closed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialQuantityBottomSheetState = getCurrentShoppingListState().isQuantityBottomSheetOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentShoppingListState().isQuantityBottomSheetOpened

        verifyMocks()
        assertThat(initialQuantityBottomSheetState).isTrue()
        assertThat(resultIngredientQuantityState).isFalse()
    }

    @Test
    fun `onCheckBoxToggled - one item`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialCheckState = getCurrentShoppingListState().checkedIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[2]))
        val resultCheckState = getCurrentShoppingListState().checkedIngredients

        verifyMocks()
        assertThat(initialCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],false),
                Pair(ingredients[1],false),
                Pair(ingredients[2],false),
                Pair(ingredients[3],false),
                Pair(ingredients[4],false)
            )
        )
        assertThat(resultCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],false),
                Pair(ingredients[1],false),
                Pair(ingredients[2],true),
                Pair(ingredients[3],false),
                Pair(ingredients[4],false)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - one item - unchecked`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[2]))
        val initialCheckState = getCurrentShoppingListState().checkedIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[2]))
        val resultCheckState = getCurrentShoppingListState().checkedIngredients

        verifyMocks()
        assertThat(initialCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],false),
                Pair(ingredients[1],false),
                Pair(ingredients[2],true),
                Pair(ingredients[3],false),
                Pair(ingredients[4],false)
            )
        )
        assertThat(resultCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],false),
                Pair(ingredients[1],false),
                Pair(ingredients[2],false),
                Pair(ingredients[3],false),
                Pair(ingredients[4],false)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - all items`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        val initialCheckState = getCurrentShoppingListState().checkedIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[3]))
        val resultCheckState = getCurrentShoppingListState().checkedIngredients

        verifyMocks()
        assertThat(initialCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],false),
                Pair(ingredients[1],false),
                Pair(ingredients[2],false),
                Pair(ingredients[3],false),
                Pair(ingredients[4],false)
            )
        )
        assertThat(resultCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],true),
                Pair(ingredients[1],true),
                Pair(ingredients[2],true),
                Pair(ingredients[3],true),
                Pair(ingredients[4],true)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - all items - unchecked`() {
        setMocksWithEmptyShoppingList()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedIngredient(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave)
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[3]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[4]))
        val initialCheckState = getCurrentShoppingListState().checkedIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[2]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[4]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[0]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[1]))
        shoppingListViewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(ingredients[3]))
        val resultCheckState = getCurrentShoppingListState().checkedIngredients

        verifyMocks()
        assertThat(initialCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],true),
                Pair(ingredients[1],true),
                Pair(ingredients[2],true),
                Pair(ingredients[3],true),
                Pair(ingredients[4],true)
            )
        )
        assertThat(resultCheckState).isEqualTo(
            mapOf(
                Pair(ingredients[0],false),
                Pair(ingredients[1],false),
                Pair(ingredients[2],false),
                Pair(ingredients[3],false),
                Pair(ingredients[4],false)
            )
        )
    }

    @Test
    fun `addShoppingList runs successfully`() {
        setMocks()
        coEvery { addShoppingListUseCase(any()) } returns flowOf(Resource.Success(true))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddShoppingList)
        val isLoading = getCurrentShoppingListState().isLoading

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID",true)
            addShoppingListUseCase(any())
        }
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `addShoppingList returns error`() {
        setMocks()
        coEvery { addShoppingListUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddShoppingList)
        val isLoading = getCurrentShoppingListState().isLoading

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID",true)
            addShoppingListUseCase(any())
        }
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `addShoppingList is loading`() {
        setMocks()
        coEvery { addShoppingListUseCase(any()) } returns flowOf(Resource.Loading(true))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnAddShoppingList)
        val isLoading = getCurrentShoppingListState().isLoading

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID",true)
            addShoppingListUseCase(any())
        }
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `getUserShoppingLists runs successfully`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().userShoppingLists
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEqualTo(shoppingLists)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserShoppingLists returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Error("Error message"))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().userShoppingLists
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserShoppingLists is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getUserShoppingListsUseCase(any(), any()) } returns flowOf(Resource.Loading(true))

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().userShoppingLists
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `getShoppingList runs successfully`() {
        setMocks()
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Success(displayedShoppingList))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedShoppingList("shoppingListId"))
        val result = getCurrentShoppingListState()
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        coVerify(exactly = 1) { getShoppingListUseCase("shoppingListId") }
        assertThat(result.shoppingListIngredients).isEqualTo(displayedShoppingList.ingredients)
        assertThat(result.displayedShoppingListId).isEqualTo(displayedShoppingList.shoppingListId)
        assertThat(result.checkedIngredients).isEqualTo(displayedShoppingList.checkedIngredients)
        assertThat(result.shoppingListName).isEqualTo(displayedShoppingList.name)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getShoppingList returns error`() {
        setMocks()
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedShoppingList("shoppingListId"))
        val result = getCurrentShoppingListState()
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        coVerify(exactly = 1) { getShoppingListUseCase("shoppingListId") }
        assertThat(result.shoppingListIngredients).isEmpty()
        assertThat(result.displayedShoppingListId).isEmpty()
        assertThat(result.checkedIngredients).isEqualTo(getCurrentShoppingListState().allIngredients.associateWith { false })
        assertThat(result.shoppingListName).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getShoppingList is loading`() {
        setMocks()
        coEvery { getShoppingListUseCase(any()) } returns flowOf(Resource.Loading(true))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.SelectedShoppingList("shoppingListId"))
        val result = getCurrentShoppingListState()
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        coVerify(exactly = 1) { getShoppingListUseCase("shoppingListId") }
        assertThat(result.shoppingListIngredients).isEmpty()
        assertThat(result.displayedShoppingListId).isEmpty()
        assertThat(result.checkedIngredients).isEqualTo(getCurrentShoppingListState().allIngredients.associateWith { false })
        assertThat(result.shoppingListName).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `deleteShoppingList runs successfully`() {
        setMocks()
        coEvery { deleteShoppingListUseCase(any()) } returns flowOf(Resource.Success(true))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnDeleteShoppingList)
        val isLoading = getCurrentShoppingListState().isLoading

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID", true)
            deleteShoppingListUseCase("shoppingListId")
            getUserShoppingListsUseCase("userUID", true)
        }
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `deleteShoppingList returns error`() {
        setMocks()
        coEvery { deleteShoppingListUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnDeleteShoppingList)
        val isLoading = getCurrentShoppingListState().isLoading

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID", true)
            deleteShoppingListUseCase("shoppingListId")
        }
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `deleteShoppingList is loading`() {
        setMocks()
        coEvery { deleteShoppingListUseCase(any()) } returns flowOf(Resource.Loading(true))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnDeleteShoppingList)
        val isLoading = getCurrentShoppingListState().isLoading

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID", true)
            deleteShoppingListUseCase("shoppingListId")
        }
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `EnteredName - changed string`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialNameState = getCurrentShoppingListState().shoppingListName

        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredName("New Name"))
        val resultNameState = getCurrentShoppingListState().shoppingListName

        verifyMocks()
        assertThat(initialNameState).isEqualTo("Shopping List Name")
        assertThat(resultNameState).isEqualTo("New Name")
    }

    @Test
    fun `EnteredName - result empty`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialNameState = getCurrentShoppingListState().shoppingListName

        shoppingListViewModel.onEvent(ShoppingListEvent.EnteredName(""))
        val resultNameState = getCurrentShoppingListState().shoppingListName

        verifyMocks()
        assertThat(initialNameState).isEqualTo("Shopping List Name")
        assertThat(resultNameState).isEmpty()
    }

    @Test
    fun `OnMenuButtonClicked - menu is opened`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialMenuState = getCurrentShoppingListState().isMenuOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnMenuButtonClicked)
        val resultMenuState = getCurrentShoppingListState().isMenuOpened

        verifyMocks()
        assertThat(initialMenuState).isFalse()
        assertThat(resultMenuState).isTrue()
    }

    @Test
    fun `OnMenuDismissed - menu is closed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnMenuButtonClicked)
        val initialMenuState = getCurrentShoppingListState().isMenuOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnMenuDismissed)
        val resultMenuState = getCurrentShoppingListState().isMenuOpened

        verifyMocks()
        assertThat(initialMenuState).isTrue()
        assertThat(resultMenuState).isFalse()
    }

    @Test
    fun `OnOpenRenameShoppingListDialog - dialog is opened`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialRenameDialogState = getCurrentShoppingListState().isRenameShoppingListDialogOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnOpenRenameShoppingListDialog)
        val resultRenameDialogState = getCurrentShoppingListState().isRenameShoppingListDialogOpened

        verifyMocks()
        assertThat(initialRenameDialogState).isFalse()
        assertThat(resultRenameDialogState).isTrue()
    }

    @Test
    fun `OnRenameShoppingListDialogSaved - dialog is closed`() {
        setMocks()
        coEvery { addShoppingListUseCase(any()) } returns flowOf(Resource.Success(true))

        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnOpenRenameShoppingListDialog)
        val initialRenameDialogState = getCurrentShoppingListState().isRenameShoppingListDialogOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnRenameShoppingListDialogSaved)
        val resultRenameDialogState = getCurrentShoppingListState().isRenameShoppingListDialogOpened

        coVerifyOrder {
            getCurrentUserUseCase()
            getIngredientsUseCase()
            getUserShoppingListsUseCase("userUID",true)
            addShoppingListUseCase(any())
        }
        assertThat(initialRenameDialogState).isTrue()
        assertThat(resultRenameDialogState).isFalse()
    }

    @Test
    fun `OnRenameShoppingListDialogDismissed - dialog is closed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnOpenRenameShoppingListDialog)
        val initialRenameDialogState = getCurrentShoppingListState().isRenameShoppingListDialogOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnRenameShoppingListDialogDismissed)
        val resultRenameDialogState = getCurrentShoppingListState().isRenameShoppingListDialogOpened

        verifyMocks()
        assertThat(initialRenameDialogState).isTrue()
        assertThat(resultRenameDialogState).isFalse()
    }

    @Test
    fun `OnDeleteAllIngredients - state is set correctly`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        val initialShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients
        val initialIngredientsToSelectState = getCurrentShoppingListState().ingredientsToSelect
        val initialCheckedIngredientsState = getCurrentShoppingListState().checkedIngredients

        shoppingListViewModel.onEvent(ShoppingListEvent.OnDeleteAllIngredients)
        val resultShoppingListIngredientsState = getCurrentShoppingListState().shoppingListIngredients
        val resultIngredientsToSelectState = getCurrentShoppingListState().ingredientsToSelect
        val resultCheckedIngredientsState = getCurrentShoppingListState().checkedIngredients

        verifyMocks()
        assertThat(initialShoppingListIngredientsState).isEqualTo(displayedShoppingList.ingredients)
        assertThat(initialIngredientsToSelectState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[3],
                ingredients[4]
            )
        )
        assertThat(initialCheckedIngredientsState).isEqualTo(displayedShoppingList.checkedIngredients)
        assertThat(resultShoppingListIngredientsState).isEqualTo(emptyMap<Ingredient, Quantity>())
        assertThat(resultIngredientsToSelectState).isEqualTo(ingredients)
        assertThat(resultCheckedIngredientsState).isEqualTo(ingredients.associateWith { false })
    }

    @Test
    fun `OnOpenOtherShoppingListsMenu - other lists menu is opened - menu is closed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnMenuButtonClicked)
        val initialMenuState = getCurrentShoppingListState().isMenuOpened
        val initialOtherListsMenuState = getCurrentShoppingListState().isOtherShoppingListsMenuOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnOpenOtherShoppingListsMenu)
        val resultMenuState = getCurrentShoppingListState().isMenuOpened
        val resultOtherListsMenuState = getCurrentShoppingListState().isOtherShoppingListsMenuOpened

        verifyMocks()
        assertThat(initialMenuState).isTrue()
        assertThat(initialOtherListsMenuState).isFalse()
        assertThat(resultMenuState).isFalse()
        assertThat(resultOtherListsMenuState).isTrue()
    }

    @Test
    fun `OnOtherShoppingListsMenuDismissed - other lists menu is closed - menu is closed`() {
        setMocks()
        shoppingListViewModel = setViewModel()
        shoppingListViewModel.onEvent(ShoppingListEvent.OnOpenOtherShoppingListsMenu)
        val initialMenuState = getCurrentShoppingListState().isMenuOpened
        val initialOtherListsMenuState = getCurrentShoppingListState().isOtherShoppingListsMenuOpened

        shoppingListViewModel.onEvent(ShoppingListEvent.OnOtherShoppingListsMenuDismissed)
        val resultMenuState = getCurrentShoppingListState().isMenuOpened
        val resultOtherListsMenuState = getCurrentShoppingListState().isOtherShoppingListsMenuOpened

        verifyMocks()
        assertThat(initialMenuState).isFalse()
        assertThat(initialOtherListsMenuState).isTrue()
        assertThat(resultMenuState).isFalse()
        assertThat(resultOtherListsMenuState).isFalse()
    }
}
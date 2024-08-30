package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
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
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var ingredients: List<Ingredient>
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        getIngredientsUseCase = mockk()
        getCurrentUserUseCase = mockk()
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
    }

    @After
    fun tearDown() {
        confirmVerified(getIngredientsUseCase)
        confirmVerified(getCurrentUserUseCase)
        clearAllMocks()
    }

    private fun setViewModel(): ShoppingListViewModel {
        return ShoppingListViewModel(
            getCurrentUserUseCase,
            getIngredientsUseCase
        )
    }

    private fun getCurrentShoppingListState(): ShoppingListState {
        return shoppingListViewModel.shoppingListState.value
    }

    private fun verifyMocks() {
        coVerifySequence {
            getCurrentUserUseCase()
            getIngredientsUseCase()
        }
    }

    @Test
    fun `checkIfUserLoggedIn - user is logged in`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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

        shoppingListViewModel = setViewModel()
        val result = getCurrentShoppingListState().ingredientsToSelect
        val isLoading = getCurrentShoppingListState().isLoading

        verifyMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `enteredIngredient - initially empty`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))

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


}
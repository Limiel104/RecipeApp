package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.ShoppingList

data class ShoppingListState(
    val isUserLoggedIn: Boolean = false,
    val userUID: String = "",
    val isAddIngredientsDialogOpened: Boolean = false,
    val ingredientsToSelect: List<Ingredient> = emptyList(),
    val allIngredients: List<Ingredient> = emptyList(),
    val shoppingListIngredients: Map<Ingredient, Quantity> = emptyMap(),
    val isDropDownMenuExpanded: Boolean = false,
    val ingredient: String = "",
    val selectedIngredients: List<Ingredient> = emptyList(),
    val isQuantityBottomSheetOpened: Boolean = false,
    val clickedIngredientId: String = "",
    val selectedWholeQuantity: String = "",
    val selectedDecimalQuantity: String = "",
    val selectedTypeQuantity: String = "",
    val checkedIngredients: Map<Ingredient, Boolean> = emptyMap(),
    val userShoppingLists: List<ShoppingList> = emptyList(),
    val isMenuOpened: Boolean = false,
    val isOtherShoppingListsMenuOpened: Boolean = false,
    val isRenameShoppingListDialogOpened: Boolean = false,
    val shoppingListName: String = "",
    val nameError: String? = null,
    val displayedShoppingListId: String = "",
    val isLoading: Boolean = false
)
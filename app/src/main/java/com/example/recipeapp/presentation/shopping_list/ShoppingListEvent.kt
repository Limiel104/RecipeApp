package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient

sealed class ShoppingListEvent {
    data class EnteredIngredient(val ingredient: String): ShoppingListEvent()
    data class SelectedIngredient(val selectedIngredient: Ingredient): ShoppingListEvent()
    object OnAddButtonClicked: ShoppingListEvent()
    object OnDropDownMenuExpandChange: ShoppingListEvent()
    object OnAddIngredientsDialogDismiss: ShoppingListEvent()
    object OnLogin: ShoppingListEvent()
    object OnSignup: ShoppingListEvent()
}
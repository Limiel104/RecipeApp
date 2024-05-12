package com.example.recipeapp.presentation.shopping_list

sealed class ShoppingListUiEvent {
    object NavigateToLogin: ShoppingListUiEvent()
    object NavigateToSignup: ShoppingListUiEvent()
}
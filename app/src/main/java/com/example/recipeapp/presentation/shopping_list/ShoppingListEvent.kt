package com.example.recipeapp.presentation.shopping_list

sealed class ShoppingListEvent {
    object OnLogin: ShoppingListEvent()
    object OnSignup: ShoppingListEvent()
}
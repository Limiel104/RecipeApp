package com.example.recipeapp.presentation.shopping_list

import com.example.recipeapp.domain.model.Ingredient

data class ShoppingListState(
    val isUserLoggedIn: Boolean = false,
    val ingredientsToSelect: List<Ingredient> = emptyList(),
    val allIngredients: List<Ingredient> = emptyList(),
    val isLoading: Boolean = false
)
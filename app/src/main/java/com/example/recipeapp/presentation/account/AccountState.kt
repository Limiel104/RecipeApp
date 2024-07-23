package com.example.recipeapp.presentation.account

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.util.RecipeOrder

data class AccountState(
    val isUserLoggedIn: Boolean = false,
    val userUID: String = "",
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val recipesOrder: RecipeOrder = RecipeOrder.DateDescending
)
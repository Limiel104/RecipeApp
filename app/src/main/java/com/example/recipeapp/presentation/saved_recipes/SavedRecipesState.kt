package com.example.recipeapp.presentation.saved_recipes

import com.example.recipeapp.domain.model.Recipe

data class SavedRecipesState(
    val isUserLoggedIn: Boolean = false,
    val userUID: String = "",
    val savedRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false
)
package com.example.recipeapp.presentation.saved_recipes

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchSuggestion

data class SavedRecipesState(
    val isUserLoggedIn: Boolean = false,
    val userUID: String = "",
    val savedRecipes: List<Recipe> = emptyList(),
    val query: String = "",
    val searchSuggestions: List<SearchSuggestion> = emptyList(),
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = false
)
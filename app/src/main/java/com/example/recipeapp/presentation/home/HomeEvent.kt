package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.util.RecipeOrder

sealed class HomeEvent {
    data class OnRecipeSelected(val recipeId: String): HomeEvent()
    data class OnQueryChange(val query: String): HomeEvent()
    data class OnSearchSuggestionClicked(val suggestionText: String): HomeEvent()
    data class OnCategoryClicked(val categoryId: String): HomeEvent()
    data class OnSortRecipes(val recipeOrder: RecipeOrder): HomeEvent()
    object OnActiveChange: HomeEvent()
    object OnSearchClicked: HomeEvent()
    object OnClearClicked: HomeEvent()
}
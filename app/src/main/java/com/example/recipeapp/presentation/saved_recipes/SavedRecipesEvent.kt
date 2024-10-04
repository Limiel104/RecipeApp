package com.example.recipeapp.presentation.saved_recipes

import com.example.recipeapp.domain.util.RecipeOrder

sealed class SavedRecipesEvent {
    data class OnRemove(val recipeId: String): SavedRecipesEvent()
    data class OnQueryChange(val query: String): SavedRecipesEvent()
    data class OnSearchSuggestionClicked(val suggestionText: String): SavedRecipesEvent()
    data class OnRecipeSelected(val recipeId: String): SavedRecipesEvent()
    data class OnSortRecipes(val recipeOrder: RecipeOrder): SavedRecipesEvent()
    object OnActiveChange: SavedRecipesEvent()
    object OnSearchClicked: SavedRecipesEvent()
    object OnClearClicked: SavedRecipesEvent()
    object OnLogin: SavedRecipesEvent()
    object OnSignup: SavedRecipesEvent()
}
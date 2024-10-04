package com.example.recipeapp.presentation.saved_recipes

sealed class SavedRecipesUiEvent {
    data class NavigateToRecipeDetails(val recipeId: String): SavedRecipesUiEvent()
    object NavigateToLogin: SavedRecipesUiEvent()
    object NavigateToSignup: SavedRecipesUiEvent()
}
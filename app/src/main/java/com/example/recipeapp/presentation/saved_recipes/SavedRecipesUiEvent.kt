package com.example.recipeapp.presentation.saved_recipes

sealed class SavedRecipesUiEvent {
    object NavigateToLogin: SavedRecipesUiEvent()
    object NavigateToSignup: SavedRecipesUiEvent()
}
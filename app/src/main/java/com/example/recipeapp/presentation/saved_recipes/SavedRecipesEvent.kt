package com.example.recipeapp.presentation.saved_recipes

sealed class SavedRecipesEvent {
    object OnLogin: SavedRecipesEvent()
    object OnSignup: SavedRecipesEvent()
}
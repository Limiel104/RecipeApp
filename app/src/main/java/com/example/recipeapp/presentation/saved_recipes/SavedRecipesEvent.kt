package com.example.recipeapp.presentation.saved_recipes

sealed class SavedRecipesEvent {
    data class OnRemove(val recipeId: String): SavedRecipesEvent()
    object OnLogin: SavedRecipesEvent()
    object OnSignup: SavedRecipesEvent()
}
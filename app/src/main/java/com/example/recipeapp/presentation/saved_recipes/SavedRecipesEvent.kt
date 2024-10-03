package com.example.recipeapp.presentation.saved_recipes

sealed class SavedRecipesEvent {
    data class OnRemove(val recipeId: String): SavedRecipesEvent()
    data class OnQueryChange(val query: String): SavedRecipesEvent()
    data class OnSearchSuggestionClicked(val suggestionText: String): SavedRecipesEvent()
    object OnActiveChange: SavedRecipesEvent()
    object OnSearchClicked: SavedRecipesEvent()
    object OnClearClicked: SavedRecipesEvent()
    object OnLogin: SavedRecipesEvent()
    object OnSignup: SavedRecipesEvent()
}
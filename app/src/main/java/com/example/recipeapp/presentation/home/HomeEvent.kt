package com.example.recipeapp.presentation.home

sealed class HomeEvent {
    data class OnRecipeSelected(val recipeId: String): HomeEvent()
    data class OnQueryChange(val query: String): HomeEvent()
    data class OnSearchSuggestionClicked(val suggestionText: String): HomeEvent()
    object OnActiveChange: HomeEvent()
    object OnSearchClicked: HomeEvent()
    object OnClearClicked: HomeEvent()
}
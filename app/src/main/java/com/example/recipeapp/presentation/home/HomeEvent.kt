package com.example.recipeapp.presentation.home

sealed class HomeEvent {
    data class OnRecipeSelected(val recipeId: String): HomeEvent()
    data class OnQueryChange(val query: String): HomeEvent()
    data class OnRecentQuerySearchClicked(val query: String): HomeEvent()
    object OnActiveChange: HomeEvent()
    object OnSearchClicked: HomeEvent()
    object OnClearClicked: HomeEvent()
}
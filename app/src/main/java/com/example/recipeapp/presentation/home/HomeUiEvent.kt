package com.example.recipeapp.presentation.home

sealed class HomeUiEvent {
    data class NavigateToRecipeDetails(val recipeId: String): HomeUiEvent()
}
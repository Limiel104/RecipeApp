package com.example.recipeapp.presentation.home

sealed class HomeEvent {
    data class OnRecipeSelected(val recipeId: String): HomeEvent()
}
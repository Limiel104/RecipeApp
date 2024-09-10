package com.example.recipeapp.presentation.recipe_details

sealed class RecipeDetailsEvent {
    data class OnTabChanged(val tabId: Int): RecipeDetailsEvent()
}
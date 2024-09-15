package com.example.recipeapp.presentation.recipe_details

sealed class RecipeDetailsEvent {
    data class OnTabChanged(val tabId: Int): RecipeDetailsEvent()
    object OnLessServings: RecipeDetailsEvent()
    object OnMoreServings: RecipeDetailsEvent()
    object OnGoBack: RecipeDetailsEvent()
}
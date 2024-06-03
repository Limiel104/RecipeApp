package com.example.recipeapp.presentation.add_recipe

sealed class AddRecipeUiEvent {
    object LaunchCamera: AddRecipeUiEvent()
    object LaunchGallery: AddRecipeUiEvent()
}
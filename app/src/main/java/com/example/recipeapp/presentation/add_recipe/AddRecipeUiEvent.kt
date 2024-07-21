package com.example.recipeapp.presentation.add_recipe

import android.net.Uri

sealed class AddRecipeUiEvent {
    data class LaunchCamera(val tempUri: Uri?): AddRecipeUiEvent()
    object LaunchGetPermission: AddRecipeUiEvent()
    object LaunchGallery: AddRecipeUiEvent()
}
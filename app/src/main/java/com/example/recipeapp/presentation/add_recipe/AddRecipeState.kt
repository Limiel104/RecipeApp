package com.example.recipeapp.presentation.add_recipe

import android.net.Uri
import com.example.recipeapp.domain.model.Ingredient

data class AddRecipeState(
    val recipeId: String = "",
    val title: String = "",
    val titleError: String? = null,
    val prepTime: String = "",
    val servings: Int = 0,
    val description: String = "",
    val descriptionError: String? = null,
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
    val imageUrl: String = "",
    val createdBy: String = "",
    val categories: List<String> = emptyList(),
    val ingredient: String = "",
    val selectedServings: Int = 0,
    val isServingsBottomSheetOpened: Boolean = false,
    val lastSavedServings: Int = 0,
    val selectedPrepTimeHours: String = "",
    val selectedPrepTimeMinutes: String = "",
    val isPrepTimeBottomSheetOpened: Boolean = false,
    val lastSavedPrepTime: String = "",
    val lastSavedPrepTimeHours: String = "",
    val lastSavedPrepMinutes: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val isDropDownMenuExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val recipeIngredients: List<Ingredient> = emptyList(),
    val isImageBottomSheetOpen: Boolean = false,
    val imageUri: Uri = Uri.EMPTY
)
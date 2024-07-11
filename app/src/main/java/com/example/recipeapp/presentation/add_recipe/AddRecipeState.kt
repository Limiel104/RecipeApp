package com.example.recipeapp.presentation.add_recipe

import android.net.Uri
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity

data class AddRecipeState(
    val recipeId: String = "",
    val title: String = "",
    val titleError: String? = null,
    val prepTime: String = "",
    val description: String = "",
    val descriptionError: String? = null,
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
    val imageUrl: String = "",
    val createdBy: String = "",
    val categories: Map<Category, Boolean> = emptyMap(),
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
    val recipeIngredients: Map<Ingredient, Quantity> = emptyMap(),
    val isImageBottomSheetOpened: Boolean = false,
    val imageUri: Uri? = Uri.EMPTY,
    val tempUri: Uri? = Uri.EMPTY,
    val dragIndex: String = "",
    val dropIndex: String = "",
    val draggedIngredientId: String = "",
    val allIngredients: List<Ingredient> = emptyList(),
    val isReorderModeActivated: Boolean = false,
    val isQuantityBottomSheetOpened: Boolean = false,
    val selectedWholeQuantity: String = "",
    val selectedDecimalQuantity: String = "",
    val selectedTypeQuantity: String = "",
    val selectedIngredientId: String = "",
    val index: Int = -1,
    val isCategoriesDialogActivated: Boolean = false,
    val lastSavedCategories: Map<Category, Boolean> = emptyMap()
)
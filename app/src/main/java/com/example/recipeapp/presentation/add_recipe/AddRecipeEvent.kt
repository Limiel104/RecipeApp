package com.example.recipeapp.presentation.add_recipe

import android.net.Uri
import com.example.recipeapp.domain.model.Ingredient

sealed class AddRecipeEvent {
    data class EnteredTitle(val title: String): AddRecipeEvent()
    data class EnteredDescription(val description: String): AddRecipeEvent()
    data class EnteredIngredient(val ingredient: String): AddRecipeEvent()
    data class SelectedServings(val servings: Int): AddRecipeEvent()
    data class SelectedPrepTimeHours(val hours: String): AddRecipeEvent()
    data class SelectedPrepTimeMinutes(val minutes: String): AddRecipeEvent()
    data class SelectedIngredient(val ingredient: Ingredient): AddRecipeEvent()
    data class SelectedRecipePicture(val photoUri: Uri?): AddRecipeEvent()
    data class PreparedTempUri(val tempUri: Uri?): AddRecipeEvent()
    object OnServingsPickerDismissed: AddRecipeEvent()
    object OnServingsPickerSaved: AddRecipeEvent()
    object OnServingsButtonClicked: AddRecipeEvent()
    object OnPrepTimePickerDismissed: AddRecipeEvent()
    object OnPrepTimePickerSaved: AddRecipeEvent()
    object OnPrepTimeButtonClicked: AddRecipeEvent()
    object OnExpandChange: AddRecipeEvent()
    object OnAddPhoto: AddRecipeEvent()
    object OnTakePhoto: AddRecipeEvent()
    object OnSelectImage: AddRecipeEvent()
    object OnAddRecipe: AddRecipeEvent()
}
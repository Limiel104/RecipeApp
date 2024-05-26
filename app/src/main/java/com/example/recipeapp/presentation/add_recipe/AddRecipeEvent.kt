package com.example.recipeapp.presentation.add_recipe

sealed class AddRecipeEvent {
    data class EnteredTitle(val title: String): AddRecipeEvent()
    data class EnteredDescription(val description: String): AddRecipeEvent()
    data class EnteredIngredient(val ingredient: String): AddRecipeEvent()
    data class SelectedServings(val servings: Int): AddRecipeEvent()
    object OnServingsPickerDismissed: AddRecipeEvent()
    object OnServingsPickerSaved: AddRecipeEvent()
    object OnServingsButtonClicked: AddRecipeEvent()
    object OnAddRecipe: AddRecipeEvent()
}
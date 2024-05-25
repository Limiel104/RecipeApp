package com.example.recipeapp.presentation.add_recipe

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
    val selectedServings: Int = 0
)
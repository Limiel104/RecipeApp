package com.example.recipeapp.domain.model

data class Recipe(
    val recipeId: String = "",
    val name: String = "",
    val ingredientList: List<String> = emptyList(),
    val prepTime: String = "",
    val servings: Int = 0,
    val description: String = "",
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
    val imageUrl: String = ""
)
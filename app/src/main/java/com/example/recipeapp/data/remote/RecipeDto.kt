package com.example.recipeapp.data.remote

typealias IngredientId = String
typealias Quantity = String

data class RecipeDto(
    val recipeId: String = "",
    val name: String = "",
    val ingredientMap: Map<IngredientId,Quantity> = emptyMap(),
    val prepTime: String = "",
    val servings: Int = 0,
    val description: String = "",
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
    val imageUrl: String = "",
    val createdBy: String = "",
    val categoryList: List<String> = emptyList(),
    val date: Long = 0
)
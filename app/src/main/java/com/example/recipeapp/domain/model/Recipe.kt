package com.example.recipeapp.domain.model

typealias UserId = String

data class Recipe(
    val recipeId: String,
    val name: String,
    val ingredientList: List<IngredientQuantity>,
    val prepTime: String,
    val servings: Int,
    val description: String,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val imageUrl: String,
    val createdBy: UserId
)
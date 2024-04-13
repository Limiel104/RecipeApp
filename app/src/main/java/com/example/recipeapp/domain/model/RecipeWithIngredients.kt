package com.example.recipeapp.domain.model

typealias Quantity = String

data class RecipeWithIngredients(
    val recipeId: String,
    val name: String,
    val ingredients: Map<Ingredient,Quantity>,
    val prepTime: String,
    val servings: Int,
    val description: String,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val imageUrl: String,
    val createdBy: String
)
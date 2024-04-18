package com.example.recipeapp.domain.model

data class ShoppingListWithIngredients(
    val shoppingListId: String,
    val name: String,
    val createdBy: UserId,
    val ingredients: Map<Ingredient, Quantity>
)
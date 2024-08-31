package com.example.recipeapp.data.remote

data class ShoppingListDto(
    val shoppingListId: String = "",
    val name: String = "",
    val createdBy: String = "",
    val ingredientMap: Map<IngredientId, Quantity> = emptyMap(),
    val checkedIngredientMap: Map<IngredientId, Boolean> = emptyMap(),
    val date: Long = 0
)
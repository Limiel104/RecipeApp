package com.example.recipeapp.domain.model

data class ShoppingList(
    val shoppingListId: String,
    val name: String,
    val createdBy: UserId,
    val date: Long
)
package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingListIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val shoppingListIngredientId: Long,
    val ingredientId: String,
    val shoppingListId: String,
    val quantity: String,
    val isChecked: Boolean
)
package com.example.recipeapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity

data class ShoppingListWithIngredient(
    @Embedded
    val shoppingList: ShoppingListEntity,
    @Relation(
        parentColumn = "shoppingListId",
        entityColumn = "shoppingListId"
    )
    val shoppingListIngredients: List<ShoppingListIngredientEntity>
)
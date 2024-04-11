package com.example.recipeapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.data.local.entity.RecipeEntity

data class RecipeWithIngredientsQuantity(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientQuantityId",
    )
    val ingredientsQuantity: List<IngredientQuantityEntity>
)
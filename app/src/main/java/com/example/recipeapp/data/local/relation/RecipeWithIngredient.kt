package com.example.recipeapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity

data class RecipeWithIngredient(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeId",
    )
    val recipeIngredients: List<RecipeIngredientEntity>
)
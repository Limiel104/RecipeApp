package com.example.recipeapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.recipeapp.data.local.entity.RecipeCategoryEntity
import com.example.recipeapp.data.local.entity.RecipeEntity

data class RecipeWithCategory(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeId"
    )
    val categories: List<RecipeCategoryEntity>
)
package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.SavedRecipeEntity
import com.example.recipeapp.data.remote.SavedRecipeDto

fun SavedRecipeDto.toSavedRecipeEntity(): SavedRecipeEntity {
    return SavedRecipeEntity(
        savedRecipeId = savedRecipeId,
        recipeId = recipeId,
        userId = recipeId
    )
}
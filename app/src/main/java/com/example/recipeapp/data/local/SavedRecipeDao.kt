package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.SavedRecipeEntity

@Dao
interface SavedRecipeDao {

    @Insert
    suspend fun insertSavedRecipes(savedRecipes: List<SavedRecipeEntity>)

    @Query(
        """
            SELECT * 
            FROM recipeentity
            JOIN savedrecipeentity ON recipeentity.recipeId = savedrecipeentity.recipeId
        """
    )
    suspend fun getSavedRecipes(): List<RecipeEntity>

    @Query("DELETE FROM savedrecipeentity")
    suspend fun deleteSavedRecipes()
}
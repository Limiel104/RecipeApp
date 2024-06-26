package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.entity.SavedRecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithCategory

@Dao
interface SavedRecipeDao {

    @Insert
    suspend fun insertSavedRecipes(savedRecipes: List<SavedRecipeEntity>)

    @Transaction
    @Query(
        """
            SELECT * 
            FROM recipeentity
            JOIN savedrecipeentity ON recipeentity.recipeId = savedrecipeentity.recipeId
        """
    )
    suspend fun getSavedRecipes(): List<RecipeWithCategory>

    @Query("DELETE FROM savedrecipeentity")
    suspend fun deleteSavedRecipes()
}
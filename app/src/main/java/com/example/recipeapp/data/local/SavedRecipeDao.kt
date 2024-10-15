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
            WHERE LOWER(name) 
            LIKE '%' || LOWER(:query) || '%'
        """
    )
    suspend fun getSavedRecipes(query: String): List<RecipeWithCategory>

    @Query(
        """
            SELECT * 
            FROM savedrecipeentity
            WHERE recipeId == :recipeId
            AND userId == :userId
        """
    )
    suspend fun getSavedRecipeId(userId: String, recipeId: String): SavedRecipeEntity

    @Query("DELETE FROM savedrecipeentity")
    suspend fun deleteSavedRecipes()
}